var express = require('express');
const app =  express()
var http = require('http').createServer(app);
const io = require('socket.io')(http)
const port = process.env.PORT || 3000

var clients = [];

// app.use('/application',route);

app.get('/notify', (req, res, next) => {
    // vamo so experimentar fazer broadcast via esse request

    io.emit("teste1", JSON.stringify({ name:req.query.name , msg:req.query.msg})); 
    res.send("Enviou!");
})

io.on("connection", e => {
    clients.push(e);
    console.log("New socket connected!");

    io.on("teste1", s => {
        console.log(s);
    })
});

http.listen(port, function(){
    console.log(`listening on *: ${port}`);
  });
