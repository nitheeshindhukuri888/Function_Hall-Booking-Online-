async function loadHalls(){
  const grid=document.getElementById('hallGrid');
  if(!grid) return;
  grid.innerHTML='<p>Loading halls...</p>';

  const city=(document.getElementById('city')?.value||'').trim();
  try{
    const r=await fetch('/api/halls?city='+encodeURIComponent(city),{cache:'no-store'});
    if(!r.ok) throw new Error('Server returned '+r.status);
    const halls=await r.json();

    if(!halls.length){
      grid.innerHTML=city
        ? `<div class="empty"><h3>No halls found in ${escapeHtml(city)}</h3><p>Try another city or <button class="btn small" onclick="showAllHalls()">Show all halls</button>.</p></div>`
        : '<div class="empty"><h3>No halls are available right now.</h3><p>Please check the server database configuration.</p></div>';
      return;
    }

    grid.innerHTML=halls.map(h=>`
      <article class="card">
        <img src="${escapeAttr(h.imageUrl||'')}" alt="${escapeAttr(h.name)}">
        <div class="card-body">
          <p class="eyebrow">${escapeHtml(h.city)}</p>
          <h3>${escapeHtml(h.name)}</h3>
          <p>${escapeHtml(h.address)}</p>
          <p>Up to ${h.capacity} guests</p>
          <p class="price">₹${Number(h.pricePerDay).toLocaleString('en-IN')} / day</p>
          <a class="btn" href="/hall.html?id=${h.id}">View details</a>
        </div>
      </article>`).join('');
  }catch(err){
    console.error(err);
    grid.innerHTML=`<div class="empty"><h3>Unable to load halls</h3><p>${escapeHtml(err.message)}</p><p>Check the Render service logs and database connection.</p></div>`;
  }
}

async function showAllHalls(){
  document.getElementById('city').value='';
  await loadHalls();
}
function escapeHtml(v){return String(v??'').replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#039;'}[c]));}
function escapeAttr(v){return escapeHtml(v);}
loadHalls();
