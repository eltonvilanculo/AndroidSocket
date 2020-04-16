
var socket = io();

start()

function start(){
    socket.on('connect',()=>{
    console.log("connected to server");
    // sendMessage();
    receiveMessage();
    isDisconnected();

    }
    )
} 

function receiveMessage (){
   
    socket.on('serverChannel', (message)=>{
        document.getElementById('connection').innerHTML =  `${JSON.stringify(message)}`;
    })


}

    
function sendMessage (){

    socket.emit("messageChannel",
    // {
    //     "user":{
    //         "id":"client",
    //         "name":"Client",
    //         "phone":"845204801"
    //     },
    //     "message":{
    //         "text":"hello there",
    //         "time":new Date().getTime()
    //     }
    // })
    )
}
   
function isDisconnected(){
    socket.on('disconnect',()=>{
        console.log("disconnected  to server");
        
        document.getElementById('connection').innerHTML =  "server is down";
    })
}

 

