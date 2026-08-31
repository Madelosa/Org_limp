/* Núcleo da aplicação: estado local para demonstração + camada preparada para API. */
const APP_CONFIG = {
    apiEnabled: false,
    apiBase: 'backend/api'
};

const STORAGE = {
    users: 'gl_users',
    tasks: 'gl_tasks',
    notifications: 'gl_notifications',
    settings: 'gl_settings',
    session: 'gl_session'
};

const seedUsers = [
    {id:1,nome:'Maria Gerente',email:'gerente@empresa.com',perfil:'gerente',ativo:true},
    {id:2,nome:'João Supervisor',email:'supervisor@empresa.com',perfil:'supervisor',ativo:true},
    {id:3,nome:'Ana Supervisora',email:'ana.supervisor@empresa.com',perfil:'supervisor',ativo:true}
];

const seedTasks = [
    {id:1,titulo:'Limpeza do salão principal',local:'Salão de vendas',data:'2026-08-26',hora:'09:00',prazo:'2026-08-26',supervisor_id:2,status:'pendente',observacao:'Priorizar corredores centrais.',criado_em:'2026-08-26T08:00:00'},
    {id:2,titulo:'Higienização dos banheiros',local:'Banheiros',data:'2026-08-26',hora:'10:30',prazo:'2026-08-26',supervisor_id:2,status:'iniciada',observacao:'Repor materiais após a execução.',criado_em:'2026-08-26T08:20:00'},
    {id:3,titulo:'Limpeza do depósito',local:'Depósito',data:'2026-08-26',hora:'14:00',prazo:'2026-08-27',supervisor_id:3,status:'em andamento',observacao:'',criado_em:'2026-08-26T08:40:00'},
    {id:4,titulo:'Limpeza da entrada',local:'Entrada principal',data:'2026-08-25',hora:'08:00',prazo:'2026-08-25',supervisor_id:2,status:'concluída',observacao:'Atividade concluída.',criado_em:'2026-08-25T08:00:00'}
];

const seedNotifications = [
    {id:1,destinatario_id:2,titulo:'Nova tarefa atribuída',mensagem:'Você recebeu a tarefa "Limpeza do salão principal".',tipo:'tarefa',lida:false,data:'2026-08-26T08:00:00'},
    {id:2,destinatario_id:1,titulo:'Tarefa atualizada',mensagem:'A tarefa "Higienização dos banheiros" foi iniciada.',tipo:'status',lida:false,data:'2026-08-26T10:00:00'}
];

function getJSON(key, fallback){
    try { return JSON.parse(localStorage.getItem(key)) ?? fallback; }
    catch { return fallback; }
}
function setJSON(key, value){ localStorage.setItem(key, JSON.stringify(value)); }
function ensureSeed(){
    if(!localStorage.getItem(STORAGE.users)) setJSON(STORAGE.users, seedUsers);
    if(!localStorage.getItem(STORAGE.tasks)) setJSON(STORAGE.tasks, seedTasks);
    if(!localStorage.getItem(STORAGE.notifications)) setJSON(STORAGE.notifications, seedNotifications);
    if(!localStorage.getItem(STORAGE.settings)) setJSON(STORAGE.settings, {empresa:'Minha Empresa',email:'gestao@empresa.com',whatsapp:'',notificarEmail:true,notificarWhatsApp:true});
}
ensureSeed();

function getSession(){ return getJSON(STORAGE.session, null); }
function requireSession(){
    const session = getSession();
    if(!session && !location.pathname.endsWith('login.html')){
        location.href='login.html';
        return null;
    }
    return session;
}
function logout(){ localStorage.removeItem(STORAGE.session); location.href='login.html'; }

function formatDate(date){
    if(!date) return '-';
    const d = new Date(date + (String(date).length===10 ? 'T00:00:00' : ''));
    return new Intl.DateTimeFormat('pt-BR',{dateStyle:'medium'}).format(d);
}
function formatDateTime(date){
    if(!date) return '-';
    return new Intl.DateTimeFormat('pt-BR',{dateStyle:'short',timeStyle:'short'}).format(new Date(date));
}
function statusClass(status){
    return {'pendente':'bg-secondary','iniciada':'bg-info text-dark','em andamento':'bg-warning text-dark','concluída':'bg-success'}[status] || 'bg-secondary';
}
function statusBadge(status){ return `<span class="badge ${statusClass(status)} status-badge">${status}</span>`; }
function userById(id){ return getJSON(STORAGE.users,[]).find(u=>Number(u.id)===Number(id)); }

function addNotification(destinatario_id, titulo, mensagem, tipo='info'){
    const list = getJSON(STORAGE.notifications,[]);
    list.unshift({
        id: Date.now(),
        destinatario_id:Number(destinatario_id),
        titulo, mensagem, tipo, lida:false,
        data:new Date().toISOString()
    });
    setJSON(STORAGE.notifications,list);
}

function notifyStatusChange(task, oldStatus, newStatus){
    if(oldStatus===newStatus) return;
    addNotification(1,'Tarefa atualizada',
        `A tarefa "${task.titulo}" mudou de "${oldStatus}" para "${newStatus}".`,'status');
}

function createTask(task){
    const tasks = getJSON(STORAGE.tasks,[]);
    const item = {...task,id:Date.now(),criado_em:new Date().toISOString()};
    tasks.unshift(item);
    setJSON(STORAGE.tasks,tasks);

    // Regra de negócio: toda nova tarefa gera notificação para o supervisor.
    addNotification(item.supervisor_id,'Nova tarefa atribuída',
        `Você recebeu a tarefa "${item.titulo}".`,'tarefa');

    return item;
}

function updateTask(id, changes){
    const tasks = getJSON(STORAGE.tasks,[]);
    const index = tasks.findIndex(t=>Number(t.id)===Number(id));
    if(index<0) return null;
    const oldStatus = tasks[index].status;
    tasks[index] = {...tasks[index],...changes};
    setJSON(STORAGE.tasks,tasks);
    notifyStatusChange(tasks[index], oldStatus, tasks[index].status);
    return tasks[index];
}

function getTasks(){ return getJSON(STORAGE.tasks,[]); }
function getNotifications(userId){
    return getJSON(STORAGE.notifications,[]).filter(n=>Number(n.destinatario_id)===Number(userId));
}
function unreadCount(userId){ return getNotifications(userId).filter(n=>!n.lida).length; }
function markNotificationRead(id){
    const list = getJSON(STORAGE.notifications,[]);
    const n = list.find(x=>Number(x.id)===Number(id));
    if(n) n.lida=true;
    setJSON(STORAGE.notifications,list);
}

function renderShell(active, role){
    const session = getSession();
    const navManager = [
        ['dashboard-gerente.html','bi-house-fill','Página Inicial'],
        ['gerente_plano.html','bi-calendar-check','Plano Semanal'],
        ['gerente_tarefas.html','bi-pencil-square','Tarefas'],
        ['gerente_relatorios.html','bi-file-earmark-bar-graph','Relatórios'],
        ['gerente_notificacoes.html','bi-bell-fill','Notificações'],
        ['usuarios.html','bi-people-fill','Usuários'],
        ['gerente_configuracoes.html','bi-gear-fill','Configurações']
    ];
    const navSupervisor = [
        ['dashboard_supervisor.html','bi-house-fill','Página Inicial'],
        ['supervisor_tarefas.html','bi-list-check','Minhas Tarefas'],
        ['supervisor_notificacoes.html','bi-bell-fill','Notificações'],
        ['perfil_supervisor.html','bi-person-circle','Meu Perfil']
    ];
    const nav = role==='supervisor' ? navSupervisor : navManager;
    document.body.insertAdjacentHTML('afterbegin',`
        <header class="app-header">
            <div class="logo">
             <img src="img/log.png" class="img-fluid" width=80px height=80px alt="Logo da empresa" /></div>
            <div class="header-user">
                <span><i class="bi bi-bell-fill"></i> <span id="headerNotif">${unreadCount(session?.id||0)}</span></span>
                <span>${session?.nome || 'Usuário'}</span>
                <div class="dropdown">
                    <button class="dropdown-toggle" data-bs-toggle="dropdown"><i class="bi bi-chevron-down"></i></button>
                    <ul class="dropdown-menu dropdown-menu-end">
                        <li><a class="dropdown-item" href="${role==='supervisor'?'perfil_supervisor.html':'perfil_gerente.html'}"><i class="bi bi-person"></i> Perfil</a></li>
                        <li><hr class="dropdown-divider"></li>
                        <li><button class="dropdown-item" onclick="logout()"><i class="bi bi-box-arrow-right"></i> Sair</button></li>
                    </ul>
                </div>
            </div>
        </header>
        <aside class="sidebar">
            <nav class="nav flex-column">
                ${nav.map(n=>`<a class="nav-link ${active===n[0]?'active':''}" href="${n[0]}"><i class="bi ${n[1]}"></i><span>${n[2]}</span></a>`).join('')}
            </nav>
        </aside>
    `);
}
function pageDate(){ return new Intl.DateTimeFormat('pt-BR',{weekday:'long',day:'2-digit',month:'long',year:'numeric'}).format(new Date()); }

document.addEventListener('DOMContentLoaded',()=>{
    if(!location.pathname.endsWith('login.html')) requireSession();
});
