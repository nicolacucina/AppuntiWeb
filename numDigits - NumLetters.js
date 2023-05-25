/* Si realizzi un piccolo server in JavaScript, eseguibile in Node.js, tale che:
 * -All’avvio si mette in ascolto sulla porta 8080.
 * -Risponde a richieste di servizio di tipo GET con URL della forma: http://localhost:8080/server?command=com&string=str
 * dove, com è uno tra i seguenti comandi {numdigits, numletters} e str è una stringa.
 * -In funzione del comando, il server restituisce il numero di cifre (numdigits) o il numero lettere maiuscole o minuscole (numletters) della stringa
 */

var express = require('express');
var app = express();

app.listen(8080, function () {
    console.log('In ascolto sulla porta 8080');
})

app.get('/server', (req, res) =>{
    var command = req.query.command;
    var string = req.query.string;

    if (command == 'numdigits') {
        let count = 0;
        for (let i = 0; i < string.length; i++) {
            //if (Number.isInteger(parseInt(string[i]))) {
            if(string[i] >= '0' && string[i] <= '9'){
                count++;
            }
        }
        res.send('Numero di cifre: ' + count);
    }else if (command == 'numletters') {
        let maiuscole = 0;  
        let minuscole = 0;

        for (let i = 0; i < string.length; i++) {
            //senza la seconda condizione conta anche spazi e altri caratteri
            //if (string[i] == string[i].toUpperCase() && string[i] != string[i].toLowerCase()){
            if (string[i] >= 'A' && string[i] <= 'Z') {
                maiuscole++;
            }
            //else if (string[i] == string[i].toLowerCase() && string[i] != string[i].toUpperCase()) {
            else if (string[i] >= 'a' && string[i] <= 'z') {
                minuscole++;
            }
        }
        res.send('Numero di lettere maiuscole: ' + maiuscole + ', Numero di lettere minuscole: ' + minuscole);
    }
})