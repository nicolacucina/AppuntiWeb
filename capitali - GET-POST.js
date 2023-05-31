/* Si realizzi un’applicazione web in JavaScript/Node.js in base alle seguenti specifiche:
 * -Dal lato utente, la pagina iniziale del servizio è una pagina di nome "capitals.html",
 * All’interno del campo di testo "Country", l’utente può specificare il nome di un Paese del mondo;
 * -Alla pressione del bottone "Send", l’applicazione emette una richiesta di tipo GET verso il server, tramite AJAX, spedendo il dato nel campo di testo.
 * -Il server risponde con un testo che riporta il nome della capitale del paese specificato, se presente nella sua base di conoscenza.
 * -Il codice Javascript lato client, ricevuto il nome della capitale, lo stampa al posto dei trattini “--“.
 * -Il lato server dell’applicazione è realizzato in Node.js, attraverso l’uso della libreria Express.
 * L’elenco dei paesi del mondo e le rispettive capitali deve essere mantenuto all’interno di un file JSON(
 * sotto forma di semplice dizionario: "paese": "capitale"), che viene caricato in memoria sotto forma di oggetto JavaScript all’avvio del server
 * -Prevedere inoltre la possibilità di inserire un nuovo paese con la sua capitale nel dizionario in RAM e poi scrivere nel file in modalità asincrona
 */

var fs = require('fs');
var filename = './countries.json'
fs.readFile(filename, 'utf-8',(err, data) =>{
    if (err) throw err
    obj = JSON.parse(data)
})
//obj = JSON.parse(fs.readFileSync(filename, 'utf-8')) //per lettura sincrona

var bp = require('body-parser')
var express = require('express');
var app = express();

app.use(bp.json())
app.use(bp.urlencoded({ extended: true }))

app.listen(8080, function () {
    console.log('In ascolto sulla porta 8080');
})

app.get('/', (req, res) =>{
    res.sendFile(__dirname + '/capitals.html')
})

app.get('/capitals', (req, res) =>{
    let country = req.query.country
    console.log("GET: "+country)
    let found = false
    for(let key in obj){
        if(country == key){
            found = true
            res.send(obj[key])
        }
    }
    if(!found){
        res.send("error")
    }
})

app.post('/capitals', (req, res) =>{
    let newcountry = req.body.newcountry
    let newcapital = req.body.newcapital
    console.log("POST: "+newcountry+","+newcapital)

    let found = false
    for(let key in obj){
        if(newcountry == key){
            found = true    
        }
    }
    if(!found){
        obj[newcountry] = newcapital
        fs.writeFile(filename, JSON.stringify(obj), (err) =>{
            if (err) throw err
            console.log('File aggiornato')
        })
        res.send("ok")
    }
     
})