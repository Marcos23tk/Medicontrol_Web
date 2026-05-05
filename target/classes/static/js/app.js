const API = "/api";

async function api(path, options = {}) {
  const res = await fetch(API + path, {
    headers: { "Content-Type": "application/json" },
    ...options,
  });
  if (!res.ok) {
    const msg = await res.text();
    throw new Error(msg || "Error en la solicitud");
  }
  const text = await res.text();
  return text ? JSON.parse(text) : null;
}

function toast(msg) {
  const t = document.createElement("div");
  t.className = "toast";
  t.textContent = msg;
  document.body.appendChild(t);
  setTimeout(() => t.remove(), 2600);
}

function requireLogin() {
  if (!localStorage.getItem("usuarioMedicontrol")) location.href = "login.html";
}

function logout() {
  localStorage.removeItem("usuarioMedicontrol");
  location.href = "login.html";
}

function initLogin() {
  document.getElementById("loginForm").addEventListener("submit", async (e) => {
    e.preventDefault();
    try {
      const data = await api("/auth/login", {
        method: "POST",
        body: JSON.stringify({ correo: correo.value, password: password.value }),
      });
      localStorage.setItem("usuarioMedicontrol", JSON.stringify(data));
      location.href = "dashboard.html";
    } catch (err) {
      toast("Credenciales incorrectas");
    }
  });
}

function badge(estado) {
  if (estado === "TOMADO") return `<span class="badge badge-ok">Tomado</span>`;
  if (estado === "OMITIDO") return `<span class="badge badge-bad">Omitido</span>`;
  if (estado === "ACTIVO") return `<span class="badge badge-ok">Activo</span>`;
  if (estado === "PAUSADO") return `<span class="badge badge-pending">Pausado</span>`;
  if (estado === "FINALIZADO") return `<span class="badge badge-blue">Finalizado</span>`;
  return `<span class="badge badge-pending">Pendiente</span>`;
}

function pacienteNombre(p) { return `${p?.nombres || ""} ${p?.apellidos || ""}`.trim(); }

async function cargarDashboard() {
  const r = await api("/reportes/resumen");
  stPacientes.textContent = r.pacientes;
  stMedicamentos.textContent = r.medicamentosActivos;
  stTomadas.textContent = r.dosisTomadasHoy;
  stCumplimiento.textContent = r.cumplimientoHoy + "%";
  barCumplimiento.style.width = r.cumplimientoHoy + "%";
  alertaPanel.textContent = r.dosisPendientesHoy > 0
    ? `Tienes ${r.dosisPendientesHoy} dosis pendientes por controlar hoy.`
    : "Excelente. No hay dosis pendientes por ahora.";

  const dosis = await api("/dosis/hoy");
  tablaHoy.innerHTML = dosis.map(d => `
    <tr>
      <td>${d.horaProgramada}</td>
      <td>${pacienteNombre(d.medicamento.paciente)}</td>
      <td>${d.medicamento.nombre} ${d.medicamento.dosis}</td>
      <td>${badge(d.estado)}</td>
    </tr>`).join("");
}

async function initPacientes() {
  await listarPacientes();
  pacienteForm.addEventListener("submit", async (e) => {
    e.preventDefault();
    const data = { dni: dni.value, nombres: nombres.value, apellidos: apellidos.value, edad: Number(edad.value), telefono: telefono.value, direccion: direccion.value, contactoEmergencia: contactoEmergencia.value, observaciones: observaciones.value };
    const id = pacienteId.value;
    await api(id ? `/pacientes/${id}` : "/pacientes", { method: id ? "PUT" : "POST", body: JSON.stringify(data) });
    // Guardar foto en localStorage si se subió una
    const fotoDni = dni.value;
    const foto = document.getElementById("fotoBase64")?.value;
    if (fotoDni && foto) localStorage.setItem("foto_" + fotoDni, foto);
    pacienteForm.reset(); pacienteId.value = "";
    if (document.getElementById("fotoBase64")) document.getElementById("fotoBase64").value = "";
    if (document.getElementById("fotoPreview")) document.getElementById("fotoPreview").innerHTML = "👤";
    toast("Paciente guardado correctamente");
    listarPacientes();
  });
}

async function listarPacientes() {
  const q = document.getElementById("buscarPaciente")?.value || "";
  const pacientes = await api(`/pacientes${q ? `?q=${encodeURIComponent(q)}` : ""}`);
  tablaPacientes.innerHTML = pacientes.map(p => `
    <tr><td>${p.dni}</td><td>${pacienteNombre(p)}<br><small>${p.edad || ""} años</small></td><td>${p.telefono || "-"}</td>
    <td><button class="btn btn-sm btn-light" onclick='editarPaciente(${JSON.stringify(p)})'>Editar</button> <button class="btn btn-sm btn-danger" onclick="eliminarPaciente(${p.id})">Eliminar</button></td></tr>`).join("");
}

function editarPaciente(p) {
  pacienteId.value = p.id; dni.value = p.dni; nombres.value = p.nombres; apellidos.value = p.apellidos; edad.value = p.edad || ""; telefono.value = p.telefono || ""; direccion.value = p.direccion || ""; contactoEmergencia.value = p.contactoEmergencia || ""; observaciones.value = p.observaciones || "";
  window.scrollTo({ top: 0, behavior: "smooth" });
}
async function eliminarPaciente(id) { if(confirm("¿Eliminar paciente?")){ await api(`/pacientes/${id}`,{method:"DELETE"}); toast("Paciente eliminado"); listarPacientes(); } }

async function initMedicamentos() {
  const pacientes = await api("/pacientes");
  pacienteId.innerHTML = pacientes.map(p => `<option value="${p.id}">${pacienteNombre(p)} - DNI ${p.dni}</option>`).join("");
  const today = new Date().toISOString().slice(0,10); fechaInicio.value = today;
  await listarMedicamentos();
  medicamentoForm.addEventListener("submit", async (e) => {
    e.preventDefault();
    const data = { pacienteId: Number(pacienteId.value), nombre: nombreMed.value, dosis: dosisMed.value, presentacion: presentacion.value, frecuencia: frecuencia.value, horaToma: horaToma.value, fechaInicio: fechaInicio.value, fechaFin: fechaFin.value, indicaciones: indicaciones.value, estado: "ACTIVO" };
    const id = medId.value;
    await api(id ? `/medicamentos/${id}` : "/medicamentos", { method: id ? "PUT" : "POST", body: JSON.stringify(data) });
    medicamentoForm.reset(); medId.value = ""; fechaInicio.value = today;
    toast("Medicamento guardado correctamente");
    listarMedicamentos();
  });
}

async function listarMedicamentos() {
  const meds = await api("/medicamentos");
  tablaMedicamentos.innerHTML = meds.map(m => `
    <tr><td>${pacienteNombre(m.paciente)}</td><td><strong>${m.nombre}</strong><br><small>${m.dosis} - ${m.frecuencia || ""}</small></td><td>${m.horaToma}</td><td>${badge(m.estado)}</td>
    <td><button class="btn btn-sm btn-danger" onclick="eliminarMedicamento(${m.id})">Eliminar</button></td></tr>`).join("");
}
async function eliminarMedicamento(id) { if(confirm("¿Eliminar medicamento?")){ await api(`/medicamentos/${id}`,{method:"DELETE"}); toast("Medicamento eliminado"); listarMedicamentos(); } }

async function listarDosisHoy() {
  const dosis = await api("/dosis/hoy");
  tablaDosisHoy.innerHTML = dosis.map(d => `
    <tr><td>${d.horaProgramada}</td><td>${pacienteNombre(d.medicamento.paciente)}</td><td>${d.medicamento.nombre}</td><td>${d.medicamento.dosis}</td><td>${badge(d.estado)}</td>
    <td><button class="btn btn-sm btn-success" onclick="cambiarDosis(${d.id},'tomado')">Tomado</button> <button class="btn btn-sm btn-warning" onclick="cambiarDosis(${d.id},'pendiente')">Pendiente</button> <button class="btn btn-sm btn-danger" onclick="cambiarDosis(${d.id},'omitido')">Omitido</button></td></tr>`).join("");
}
async function cambiarDosis(id, estado) { await api(`/dosis/${id}/${estado}`, {method:"POST"}); toast("Estado actualizado"); listarDosisHoy(); }

async function listarHistorial() {
  const rows = await api("/dosis/historial");
  tablaHistorial.innerHTML = rows.map(d => `<tr><td>${d.fecha}</td><td>${d.horaProgramada}</td><td>${pacienteNombre(d.medicamento.paciente)}</td><td>${d.medicamento.nombre} ${d.medicamento.dosis}</td><td>${badge(d.estado)}</td><td>${d.marcadoEn ? d.marcadoEn.replace('T',' ') : '-'}</td></tr>`).join("");
}

async function cargarReportes() {
  const r = await api("/reportes/resumen");
  rpProgramadas.textContent = r.dosisProgramadasHoy; rpTomadas.textContent = r.dosisTomadasHoy; rpOmitidas.textContent = r.dosisOmitidasHoy; rpCumplimiento.textContent = r.cumplimientoHoy + "%"; rpBar.style.width = r.cumplimientoHoy + "%";
  recomendacion.textContent = r.cumplimientoHoy >= 80 ? "Nivel verde: buen cumplimiento del tratamiento." : r.cumplimientoHoy >= 50 ? "Nivel amarillo: cumplimiento regular, se recomienda seguimiento." : "Nivel rojo: bajo cumplimiento, revisar dosis pendientes.";
}
