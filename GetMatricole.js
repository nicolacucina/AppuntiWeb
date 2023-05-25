/*
Scrivi in JavaScript un piccolo server eseguibile in Node.js tale che:
-all’avvio si mette in ascolto sulla porta 8081 e carica da un suo file locale in formato JSON, un
dizionario le cui chiavi sono numeri di matricola e i valori sono studenti. Il file JSON è del tipo 
mostrato qui di seguito:
{
"123456":"Mario Rossi",
"123457":"Lara Bianchi",
"123458":"Giuseppe Verdi",

}
-risponde a richieste di servizio di tipo GET con URL della forma: http://localhost:8081/server?mat=xxxxxx 
(dove xxxxxx è un numero di matricola), inviando in risposta al client il nome corrispondente alla 
matricola specificata come parametro. 
Se ad esempio la richiesta fosse: http://localhost:8081/server?mat=123456
il server invierebbe in risposta la stringa "Mario Rossi".
Note: si può utilizzare il package "express" per avere una implementazione più semplice; per testare 
testare il server, si può digitare l’URL della richiesta direttamente nella barra di ricerca del browser o 
tramite il comando curl dal prompt del sistema operativo.
(esempio: curl –i http://localhost:8080/server?mat=123456
*/

var fs = require('fs');
var filename = './matricole.txt'
fs.readFile(filename, 'utf-8',(err, data) =>{
    if (err) throw err
    obj = JSON.parse(data)
})

var express = require('express')
var app = express()

app.listen(8081, () =>{
    console.log('In ascolto sulla porta 8081')
})

app.get('/matricola', (req,res) =>{
    var matricola = req.query.mat
    var result = ''
    Object.keys(obj).forEach(function(key){
        if(key == matricola){
            result = obj[key]
        }
    })
    res.end(result)
})