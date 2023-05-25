/*
Scrivi in JavaScript un piccolo server eseguibile in Node.js tale che:
-all'avvio si mette in ascolto sulla porta 8080 e carica da un suo file locale in formato JSON una semplice rubrica telefonica della forma:
{
"Rossi#Mario":"3454623488",
"Bianchi#Franca":"3339416545",
"Rossi#Giada":"3239916542",
}
Dove ogni coppia Cognome#Nome è una stringa senza spazi.
-risponde a richieste di servizio di tipo GET con URL della forma:http://localhost:8080/server?cognome=xxx 
(dove xxx è il cognome di una utenza), inviando in risposta al client la lista delle utenze (con relativo 
numero di telefono) che hanno il cognome specificato. 
Se ad esempio la richiesta fosse: http://localhost:8081/server?cognome=Rossi
il server invierebbe in risposta la lista: "[Rossi Mario – 3454623488, Rossi Giada –239916542]".
Note: si può utilizzare il package “express”; per testare testare il server, si può digitare l'URL della 
richiesta direttamente nella barra di ricerca del browser o tramite il comando curl dal prompt del 
sistema operativo (esempio: curl –i http://localhost:8080/server?cognome=Rossi)
*/

var fs = require('fs')
var filename = './agenda.txt'
fs.readFile(filename, 'utf-8', (err, data) =>{
    if(err) throw err;
    obj = JSON.parse(data)
})

const express = require('express')
const app = express()

app.listen(8081, () =>{
    console.log('In ascolto sulla porta 8081')
});

app.get('/server', (req, res) => {
    var cognome = req.query.cognome;
    console.log(cognome)
    var result = '"['
    console.log(Object.keys(obj ))
    Object.keys(obj).forEach(function(key) {
        //console.log('Key : ' + key + ', Value : ' + data[key])
        var keyParts = key.split('#')
        console.log(keyParts)
        if(keyParts[0] == cognome){
            result += ' '+keyParts[0]+' '+keyParts[1]+' - '+obj[key]+',' 
        }
    })
    result += '"]'
    res.end(result)
});
