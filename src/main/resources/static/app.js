const api = '/api';
const $ = (selector) => document.querySelector(selector);
const state = { visits: [], occupied: [], slots: [] };

function showMessage(text, type = 'success') {
  const box = $('#message');
  box.textContent = text;
  box.className = `message ${type}`;
  window.setTimeout(() => { box.className = 'message'; }, 4500);
}

async function request(path, options = {}) {
  const response = await fetch(`${api}${path}`, { headers: { 'Content-Type': 'application/json' }, ...options });
  const body = await response.json().catch(() => ({}));
  if (!response.ok) throw new Error(body.message || body.error || 'Request failed. Please try again.');
  return body;
}

const asArray = (payload) => Array.isArray(payload) ? payload : (payload.content || payload.data || []);
const value = (item, ...keys) => keys.map(key => item?.[key]).find(item => item !== undefined && item !== null);
const time = (raw) => raw ? new Date(raw).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }) : '—';

function renderOccupied() {
  const list = $('#occupied-list');
  if (!state.occupied.length) { list.innerHTML = '<div class="loading-row">All visitor bays are free.</div>'; return; }
  list.innerHTML = state.occupied.map(item => {
    const id = value(item, 'id', 'visitorVehicleId', 'visitorId');
    return `<div class="slot-row"><span class="slot-number">S-${value(item, 'slotNumber', 'parkingSlotId', 'id') || '—'}</span><div><div class="slot-vehicle">${value(item, 'vehicleNumber', 'vehicle') || 'Vehicle'}</div><div class="slot-flat">Flat ${value(item, 'flatNumber', 'flatId') || '—'} · since ${time(value(item, 'entryTime', 'enteredAt'))}</div></div><button class="release-button" data-exit="${id}">Release</button></div>`;
  }).join('');
  list.querySelectorAll('[data-exit]').forEach(button => button.addEventListener('click', () => releaseVisit(button.dataset.exit)));
}

function renderLog() {
  const body = $('#log-body');
  if (!state.visits.length) { body.innerHTML = '<tr><td colspan="6" class="empty-cell">No visits recorded today.</td></tr>'; return; }
  body.innerHTML = state.visits.map(item => `<tr><td class="mono">${value(item, 'vehicleNumber', 'vehicle') || '—'}</td><td>${value(item, 'flatNumber', 'flatId') || '—'}</td><td><span class="badge">S-${value(item, 'slotNumber', 'parkingSlotId') || '—'}</span></td><td>${time(value(item, 'entryTime', 'enteredAt'))}</td><td>${time(value(item, 'exitTime', 'exitedAt'))}</td><td>${!value(item, 'exitTime', 'exitedAt') ? `<button class="release-button" data-exit="${value(item, 'id', 'visitorVehicleId')}">Release</button>` : ''}</td></tr>`).join('');
  body.querySelectorAll('[data-exit]').forEach(button => button.addEventListener('click', () => releaseVisit(button.dataset.exit)));
}

async function refresh() {
  try {
    const [occupied, visits, slots] = await Promise.all([request('/visitors/occupied'), request('/visitors/daily-log'), request('/parking-slots')]);
    state.occupied = asArray(occupied); state.visits = asArray(visits); state.slots = asArray(slots);
    $('#occupied-count').textContent = state.occupied.length; $('#slot-count').textContent = state.slots.length; $('#visit-count').textContent = state.visits.length; $('#hero-count').textContent = state.visits.length;
    renderOccupied(); renderLog();
  } catch (error) { showMessage(error.message, 'error'); $('#occupied-list').innerHTML = '<div class="loading-row">Could not load parking data.</div>'; }
}

async function releaseVisit(id) {
  if (!id || id === 'undefined') { showMessage('This visit does not have a valid ID.', 'error'); return; }
  try { await request(`/visitors/${id}/exit`, { method: 'PATCH' }); showMessage('Vehicle released and slot is available again.'); await refresh(); }
  catch (error) { showMessage(error.message, 'error'); }
}

$('#entry-form').addEventListener('submit', async (event) => {
  event.preventDefault();
  const form = new FormData(event.currentTarget);
  try {
    await request('/visitors', { method: 'POST', body: JSON.stringify({ vehicleNumber: form.get('vehicleNumber'), flatId: Number(form.get('flatId')), parkingSlotId: Number(form.get('parkingSlotId')) }) });
    event.currentTarget.reset(); showMessage('Visitor entry recorded.'); await refresh();
  } catch (error) { showMessage(error.message, 'error'); }
});
$('#refresh-occupied').addEventListener('click', refresh); $('#refresh-log').addEventListener('click', refresh);
$('#current-date').textContent = new Date().toLocaleDateString([], { day: 'numeric', month: 'short', year: 'numeric' });
refresh();
