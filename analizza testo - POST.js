/* Si realizzi una piccola applicazione web in JavaScript/Node.js tale che:
 * -All’avvio si mette in ascolto sulla porta 8080.
 * -L’accesso al servizio è una pagina html contenente un form in cui è possibile inserire un
 * testo da inviare al server tramite una richiesta POST; la richiesta al server avviene tramite una chiamata AJAX.
 * -Il server risponde fornendo il numero di caratteri totali del testo e il numero di spazi, in un unico oggetto JSON.
 * -I dati restituiti dal server sono visualizzati nella stessa pagina iniziale, sotto al form.
Nota: nella richiesta post con AJAX, prima di effettuare la chiamata al metodo send, impostare
questa l’header del pacchetto con questa istruzione (xhr è l’oggetto XMLHTTPRequest):
xhr.setRequestHeader("Content-type","application/x-www-form-urlencoded"); */

var express = require('express');
var app = express();
app.use(express.urlencoded({ extended: true }))

app.listen(8080, function () {
    console.log('In ascolto sulla porta 8080');
})

app.get('/server', (req, res) =>{
    res.sendFile(__dirname + '/analizzaTesto.html')
})

app.post('/server', (req, res) =>{
    var string = req.body.testo
    console.log(string)
    var caratteri = 0
    var spazi = 0
    var result = {}

    for (let i = 0; i < string.length; i++) {
        if(string[i] == " "){
            spazi++
        }else if(string[i] != " "){
            caratteri++
        }
    }
        
    result.spazi = spazi
    result.caratteri = caratteri    
    res.send(result)
})

