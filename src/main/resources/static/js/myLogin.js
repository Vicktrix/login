import dom from "./dom.js";
// -------- DOM MANIPULATED --------------------
const createAcc = () => {
    dom.input.logEmail.style.display = 'inline';
    dom.input.logFullName.style.display = 'inline';
    dom.input.logRepeatPass.style.display = 'inline';
    dom.btn.registration.style.display = 'none';
    dom.btn.create.style.display = 'inline';
    dom.btn.back.style.display = 'inline';
    dom.btn.login.style.display = 'none';
};
const backAcc = () => {
    dom.input.logEmail.style.display = 'none';
    dom.input.logRepeatPass.style.display = 'none';
    dom.input.logFullName.style.display = 'none';
    dom.btn.registration.style.display = 'inline';
    dom.btn.create.style.display = 'none';
    dom.btn.back.style.display = 'none';
    dom.btn.login.style.display = 'inline';
};
//------------------------------------------------------
const getToken = () => localStorage.getItem("token");
const setToken = tk => localStorage.setItem("token",tk);
const getJSON = res => {
    if(!res.ok) { throw new Error("We can`t fetch the resource");}
    console.log('Inside get gson');
    return res.json();
};
const myError = res => {
    let message = res.toString();
    console.log('Somes happens, mistake - '+res);
    console.log('Somes happens, mistake - '+message);
    dom.output.getError.InnerHTML = '<p>'+message+'</p>';
    dom.output.getError.style.display = 'block';
    dom.input.logName.value = "YOU ARE NOT LOGGED";
    dom.input.logName.style.color = 'red';
};
const clearMyError = () => {
    dom.output.getError.InnerHTML = '';
    dom.output.getError.style.display = 'none';
    dom.input.logName.value = "";
    dom.input.logName.style.color = 'black';
};
//const notLogged = res => {
//    dom.input.logName.value = "YOU ARE NOT LOGGED";
//    dom.input.logName.style.color = 'red';
//};
const parseMyProfile = data => {
    dom.output.myProfile.innerHTML = 
    `<h3>principal name - ${data.userName}<h3/>
    id = ${data.AppUser.id}<br>
    name from DB : ${data.AppUser.username}<br>
    mail :${data.AppUser.email}<br>
    full name : ${data.AppUser.fullName}<br>
    password (encrypted) : <br> ${data.AppUser.password}<br>
    users role ${data.AppUser.role}<br>
    createDate : ${data.AppUser.createDate}<br>`;
};
const loginView = data => {
        console.log('data = ');
        console.log(data);
        console.log("data.token = ");
        console.log(data.token);
        setToken(data.token);
        dom.block.anonimLog.style.display = 'none';
        dom.block.userLog.style.display = 'block';
        dom.output.helloUser.textContent = data.user.username + '!!!';
        console.log('data.user.username  '+data.user.username);    
};
function funcLogOut() {
        setToken(" ");
        dom.block.anonimLog.style.display = 'block';
        dom.block.userLog.style.display = 'none';
        dom.output.myProfile.style.display = 'none';
        dom.input.logPass.value = "";
        dom.input.logName.value = "";
        dom.output.myProfile.innerHTML = "";
        clearMyError();
        console.log('you logOuted');    
}
// ------------ LOGIN ---------------------
const loginAcc = () => {
//    let url = 'http://localhost:8080/login';
    let url = 'http://localhost:8080/api/v1/guest/login';
    let dto = {username: dom.input.logName.value, pass: dom.input.logPass.value};
    console.log('dto = ');
    console.log(dto);
    clearMyError();
    myPost(url, dto, loginView);
};
const getProfile = () => {
    let url = 'http://localhost:8080/api/v1/profile';
    dom.output.myProfile.style.display = 'block';
    myGetAutorize(url, parseMyProfile);
};
const myPost = (url, dto, callback) => {
    fetch(url, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'}, 
        body: JSON.stringify(dto)})
    .then(getJSON)
    .then(callback)
//    .catch(notLogged);
    .catch(myError);
};
const myGet = (url, callback) => {
    fetch(url)
    .then(getJSON)
    .then(callback)
    .catch(myError);
};
const myGetAutorize = (url, callback) => {
    fetch(url, {
        method: 'GET',
        headers: {'Authorization': 'Bearer '+getToken()}})
    .then(getJSON)
    .then(callback)
    .catch(myError);
};
const funcRegister = () => {
    if(dom.input.logPass.value !== dom.input.logRepeatPass.value) {
        console.log('Pass not same as repeatPass');
    } else {
        let dto = {
            username: dom.input.logName.value,
            fullName: dom.input.logFullName.value,
            email: dom.input.logEmail.value,
            pass: dom.input.logPass.value
        };
        console.log('We send register dto :');
        console.log(dto);
        clearMyError();
        myPost('http://localhost:8080/api/v1/guest/register', dto, backAcc);
    };
};
//-----------------Log As Admin -----------------------------------
const funcLogAsAdmin = () => {
    dom.block.userLog.style.display = 'none';
    dom.block.adminLog.style.display = 'block';
    dom.output.myProfile.style.display = 'none';
//    dom.btn.contAdmin.style.display = 'disabled';
    console.log("press log as admin");
};
const funcBackAsAdmin = () => {
    dom.block.adminLog.style.display = 'none';
    dom.block.userLog.style.display = 'block';
    dom.output.getAll.style.display = 'none';
};
const funcContAsAdmin = ()  => {
    alert("This functionality not realized yet");
};
const funcGetAll = () => {
    dom.output.getAll.style.display = 'block';
    myGetAutorize("http://localhost:8080/api/v1/all", res => {
        let result = "";
        let byOne = one => { result+=    
                `<p>ID - ${one.id}</p>
                <p>User name - ${one.username}</p>
                <p>FullName - ${one.fullName}</p>
                <p>email - ${one.email}</p>
                <p>password - ${one.password}</p>
                <p>create date - ${one.createDate}</p>
                <p>roles - ${one.role}</p> <br>`;};
        res.forEach(byOne);
        dom.output.getAll.innerHTML = result;
    });
};
//---------------- ANONYMOUS --------------------------------------
dom.btn.registration.addEventListener('click', createAcc);
dom.btn.create.addEventListener('click', funcRegister);
dom.btn.back.addEventListener('click', backAcc);
dom.btn.login.addEventListener('click', loginAcc);
// --------------- USER -------------------
dom.btn.getProfile.addEventListener('click', getProfile);
dom.btn.logOut.addEventListener('click', funcLogOut);
// --------------- ADMIN -------------------
dom.btn.logAdmin.addEventListener('click', funcLogAsAdmin);
dom.btn.backToUser.addEventListener('click', funcBackAsAdmin);
dom.btn.contAdmin.addEventListener('click', funcContAsAdmin);
dom.btn.getAll.addEventListener('click', funcGetAll);