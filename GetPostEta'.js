var dictionary = new Array()

var express = require('express')
var app = express()
app.use(express.urlencoded({ extended: true }));

app.listen(8081, () =>{
    console.log('In ascolto sulla porta 8081')
})

//curl -i -X POST -d "nome=Mario&anni=18" "http://localhost:8081/server"
//curl -i -X POST -d "nome=Luigi&anni=20" "http://localhost:8081/server"
//curl -i -X POST -d "nome=Gigino&anni=15" "http://localhost:8081/server"

app.post('/server', (req,res) =>{
    var name = req.body.nome
    var age = req.body.anni

    emptyFlag = true
    if(emptyFlag){
        emptyFlag = false
        var entry = {}
        entry.nome = name
        entry.anni = age
        dictionary.push(entry)
        console.log('Valore aggiunto')
    }else{
        dictionary.forEach(function(item){
            if(item.nome == name){
                item.anni = age
                console.log('Valore aggiornato')
            }else{
                var entry = {}
                entry.nome = name
                entry.anni = age
                dictionary.push(entry)
                console.log('Valore aggiunto')
            }
        })
    
    }
    console.log(dictionary)
})

//http://localhost:8081/server?limite=16
app.get('/server', (req,res) =>{
    var limite = req.query.limite
    var result = ''
    
    for(let i in dictionary){
        if(dictionary[i].anni > limite){
            result += dictionary[i].nome + ', '
        }
    }
    console.log(result)
    res.end(result)
})