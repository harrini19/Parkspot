const getApiBase = () => {
  const origin = window.location.origin;
  if (!origin || origin === 'null' || window.location.protocol === 'file:') {
    return 'http://localhost:8080/api';
  }
  if (window.location.port && window.location.port !== '8080') {
    return 'http://localhost:8080/api';
  }
  return '/api';
};
const api = getApiBase();
const $ = (selector) => document.querySelector(selector);
const state = { visits: [], occupied: [], slots: [], flats: [] };

function showMessage(text, type = 'success') {
const box = $('#message');
box.textContent = text;
box.className = `message ${type}`;
window.setTimeout(() => {
box.className = 'message';
}, 4500);
}

async function request(path, options = {}) {
const response = await fetch(`${api}${path}`, {
cache: 'no-store',
...options,
headers: {
'Content-Type': 'application/json',
'Cache-Control': 'no-cache',
...(options.headers || {})
}
});

const text = await response.text();
let body = {};

try {
body = text ? JSON.parse(text) : {};
} catch {
body = { message: text };
}

if (!response.ok) {
throw new Error(
body.message ||
body.error ||
`Request failed with status ${response.status}`
);
}

return body;
}

const asArray = (payload) =>
Array.isArray(payload)
? payload
: (payload.content || payload.data || []);

const value = (item, ...keys) =>
keys
.map(key => item?.[key])
.find(item => item !== undefined && item !== null);

const time = (raw) =>
raw
? new Date(raw).toLocaleTimeString([], {
hour: '2-digit',
minute: '2-digit'
})
: '—';

function getSlotLabel(item) {
const slot = value(item, 'slotNumber', 'parkingSlotNumber');

if (slot) return slot;

const slotId = value(item, 'parkingSlotId', 'slotId');

return slotId ? `Slot ${slotId}` : '—';
}

function renderAvailableSlots() {
const select = $('#slot-id');
const submit = $('#entry-submit');
const selectedSlot = select.value;
const availableSlots = state.slots.filter(slot => slot.active && !slot.occupied);

select.innerHTML = '';

if (!availableSlots.length) {
const option = document.createElement('option');
option.value = '';
option.textContent = 'Parking is full';
select.appendChild(option);
select.disabled = true;
submit.disabled = true;
return;
}

const placeholder = document.createElement('option');
placeholder.value = '';
placeholder.textContent = 'Select an available slot';
placeholder.selected = true;
select.appendChild(placeholder);

availableSlots.forEach(slot => {
const option = document.createElement('option');
option.value = slot.id;
option.textContent = `Slot ${slot.slotNumber} - Available`;
select.appendChild(option);
});

if (availableSlots.some(slot => String(slot.id) === selectedSlot)) {
select.value = selectedSlot;
}

select.disabled = false;
submit.disabled = false;
}

function renderFlats(errorMessage = null) {
const select = $('#flat-number');
if (!select) return;

if (errorMessage) {
select.innerHTML = '';
const errorOption = document.createElement('option');
errorOption.value = '';
errorOption.textContent = errorMessage;
select.appendChild(errorOption);
select.disabled = true;
return;
}

  const currentSelected = select.value;

  const sortedFlats = [...state.flats].sort((a, b) =>
    (a.flatNumber || '').localeCompare(b.flatNumber || '', undefined, { numeric: true, sensitivity: 'base' })
  );

  // API hasn't returned flats yet — keep whatever pre-populated options are already in the DOM.
  if (!sortedFlats.length && select.options.length > 1) {
    select.disabled = false;
    return;
  }

  const existingValues = Array.from(select.options).slice(1).map(opt => opt.value);
  const newValues = sortedFlats.map(f => f.flatNumber);
  const isUpToDate = existingValues.length === newValues.length &&
    existingValues.every((val, i) => val === newValues[i]);

  if (isUpToDate && select.options.length > 1) {
    if (currentSelected && sortedFlats.some(f => f.flatNumber === currentSelected)) {
      select.value = currentSelected;
    }
    select.disabled = false;
    return;
  }

  select.innerHTML = '';


const placeholder = document.createElement('option');
placeholder.value = '';
placeholder.textContent = 'Select a flat';
select.appendChild(placeholder);

if (!sortedFlats.length) {
  const errorOption = document.createElement('option');
  errorOption.value = '';
  errorOption.textContent = 'Could not load flats. Please refresh the page.';
  select.innerHTML = '';
  select.appendChild(errorOption);
  select.disabled = true;
  return;
}

sortedFlats.forEach(flat => {
const option = document.createElement('option');
option.value = flat.flatNumber;
option.textContent = flat.ownerName
  ? `${flat.flatNumber} - ${flat.ownerName}`
  : flat.flatNumber;
select.appendChild(option);
});

if (currentSelected && sortedFlats.some(f => f.flatNumber === currentSelected)) {
select.value = currentSelected;
} else {
select.value = '';
}

select.disabled = false;
}

async function loadFlats() {
try {
const flats = await request('/flats');
state.flats = asArray(flats);
renderFlats();
} catch (err) {
if (!state.flats.length) {
  renderFlats('Could not load flats. Please refresh the page.');
  showMessage('Could not load flats. Please refresh the page.', 'error');
}
}
}

function renderOccupied() {
const list = $('#occupied-list');

if (!state.occupied.length) {
list.innerHTML =
'<div class="loading-row">All visitor bays are free.</div>';
return;
}

list.innerHTML = state.occupied.map(item => {
const id = value(
item,
'id',
'visitorVehicleId',
'visitorId'
);

const slotLabel = getSlotLabel(item);

const vehicle =
  value(item, 'vehicleNumber', 'vehicle') || 'Vehicle';

const flat =
  value(item, 'flatNumber', 'flatId') || '—';

const entry =
  value(item, 'entryTime', 'enteredAt');

return `
  <div class="slot-row">
    <span class="slot-number">${slotLabel}</span>

    <div>
      <div class="slot-vehicle">${vehicle}</div>

      <div class="slot-flat">
        Flat ${flat} · since ${time(entry)}
      </div>
    </div>

    <button
      class="release-button"
      data-exit="${id}">
      Release
    </button>
  </div>
`;

}).join('');

list
.querySelectorAll('[data-exit]')
.forEach(button => {
button.addEventListener(
'click',
() => releaseVisit(button.dataset.exit)
);
});
}

function renderLog() {
const body = $('#log-body');

if (!state.visits.length) {
body.innerHTML =
'<tr><td colspan="6" class="empty-cell">No visits recorded today.</td></tr>';
return;
}

body.innerHTML = state.visits.map(item => {
const exitTime =
value(item, 'exitTime', 'exitedAt');

const id =
  value(item, 'id', 'visitorVehicleId');

return `
  <tr>
    <td class="mono">
      ${value(item, 'vehicleNumber', 'vehicle') || '—'}
    </td>

    <td>
      ${value(item, 'flatNumber', 'flatId') || '—'}
    </td>

    <td>
      <span class="badge">
        ${getSlotLabel(item)}
      </span>
    </td>

    <td>
      ${time(value(item, 'entryTime', 'enteredAt'))}
    </td>

    <td>
      ${time(exitTime)}
    </td>

    <td>
      ${
        !exitTime
          ? `<button
               class="release-button"
               data-exit="${id}">
               Release
             </button>`
          : ''
      }
    </td>
  </tr>
`;

}).join('');

body
.querySelectorAll('[data-exit]')
.forEach(button => {
button.addEventListener(
'click',
() => releaseVisit(button.dataset.exit)
);
});
}

async function refresh() {
if (refreshInFlight) {
return refreshInFlight;
}

refreshInFlight = (async () => {
try {
const [occupied, visits, slots] = await Promise.all([
request('/visitors/occupied'),
request('/visitors/daily-log'),
request('/parking-slots')
]);

state.occupied = asArray(occupied);
state.visits = asArray(visits);
state.slots = asArray(slots);

$('#occupied-count').textContent =
  state.occupied.length;

$('#slot-count').textContent =
  state.slots.length;

$('#visit-count').textContent =
  state.visits.length;

$('#hero-count').textContent =
  state.visits.length;

renderOccupied();
renderLog();
renderAvailableSlots();

} catch (error) {
state.slots = [];
showMessage(error.message, 'error');

$('#occupied-list').innerHTML =
  '<div class="loading-row">Could not load parking data.</div>';
renderAvailableSlots();

}

await loadFlats();
})();

try {
await refreshInFlight;
} finally {
refreshInFlight = null;
}
}

let refreshInFlight = null;

async function releaseVisit(id) {
if (!id || id === 'undefined') {
showMessage(
'This visit does not have a valid ID.',
'error'
);
return;
}

try {
await request(`/visitors/${id}/exit`, {
method: 'PATCH'
});

await refresh();

showMessage(
  'Vehicle released. The parking slot is available again.'
);

} catch (error) {
showMessage(error.message, 'error');
}
}

$('#entry-form').addEventListener(
'submit',
async (event) => {
event.preventDefault();

const formElement = event.currentTarget;
const form = new FormData(formElement);

const vehicleNumber =
  String(form.get('vehicleNumber') || '').trim().toUpperCase();

const VEHICLE_RE = /^[A-Z0-9][A-Z0-9 \-]*$/;

if (!vehicleNumber) {
  showMessage('Vehicle number is required.', 'error');
  return;
}

if (!VEHICLE_RE.test(vehicleNumber) || vehicleNumber.length > 20) {
  showMessage('Enter a valid vehicle number (letters, digits, spaces, hyphens only; must not start with - or a symbol).', 'error');
  return;
}

const flatNumber =
  String(form.get('flatNumber') || '').trim();

const parkingSlotId =
  Number(form.get('parkingSlotId'));

if (!flatNumber) {
  showMessage('Please select a flat.', 'error');
  return;
}

const isValidFlat = state.flats.length > 0
  ? state.flats.some(flat => flat.flatNumber.toLowerCase() === flatNumber.toLowerCase())
  : Array.from($('#flat-number').options).some(opt => opt.value && opt.value.toLowerCase() === flatNumber.toLowerCase());

if (!isValidFlat) {
  showMessage('Please select a valid flat.', 'error');
  return;
}

if (!parkingSlotId) {
  showMessage('Please select an available parking slot.', 'error');
  return;
}

try {
  const result = await request('/visitors', {
    method: 'POST',
    body: JSON.stringify({
      vehicleNumber,
      flatNumber,
      parkingSlotId
    })
  });

  await refresh();

  const slotNumber = result.slotNumber || `Slot ${parkingSlotId}`;

  showMessage(
    `Parking slot ${slotNumber} reserved for you right now.`,
    'success'
  );

  formElement.reset();

} catch (error) {
  showMessage(
    error.message,
    'error'
  );
}

}
);

$('#refresh-occupied')
.addEventListener('click', refresh);

$('#refresh-log')
.addEventListener('click', refresh);

$('#current-date').textContent =
new Date().toLocaleDateString([], {
day: 'numeric',
month: 'short',
year: 'numeric'
});

loadFlats();
refresh();
window.setInterval(refresh, 4000);
