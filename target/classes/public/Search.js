let updateuuid = "";
async function getJSONData() {
    let json = await fetchPostAsync()
    
    console.log(json)
    let tbody = document.getElementById("tbd")
    tbody.innerHTML = "";

    json.map(i=>{
        let tr = document.createElement("TR");
        let forselect  = document.createElement("TD");
        forselect.classList.add("selected")
        let id  = document.createElement("TD");
        id.innerText = i.id;

        let uuid  = document.createElement("TD");
        uuid.innerText = i.uuid;

        let model  = document.createElement("TD");
        model.innerText = i.model;

        let year  = document.createElement("TD");
        year.innerText = i.year;

        let airbags  = document.createElement("TD");
        let str = "";
        i.airbags.map(airbag=>{
            str += airbag.description + ":" + airbag.value + "\n"
        })
        airbags.innerText = str;
        let color  = document.createElement("TD");
        color.classList.add("colorparent");
        let divcolor = document.createElement("DIV")
        color.appendChild(divcolor)
        divcolor.classList.add("colors")
        divcolor.style.backgroundColor = i.color;

        let models = ["Mercedes","Ferrari","Renault","Fiat","Polonez","Opel","Mini"]
        let photo  = document.createElement("TD");
        photo.classList.add("colorparent");
        let photodiv = document.createElement("img")
        photo.appendChild(photodiv)
        photodiv.classList.add("photo")
        if( models.includes( i.model)){
            photodiv.src = `/imgs/${i.model}.jpg`;
        }


        let invoicebt = document.createElement("button")
        invoicebt.innerText = "Generuj fakture"
        invoicebt.classList.add("btn")
        invoicebt.onclick = async () => {
            const data = JSON.stringify({
                uuid: i.uuid

            })
            const options = {
                method: "POST",
                body: data,
            };
            let response = await fetch("/invoice", options)
            if (!response.ok)
                return response.status
            else
                getJSONData()
            return await response.json() // response.json

        }
        tr.appendChild(forselect)
        tr.appendChild(id)
        tr.appendChild(uuid)
        tr.appendChild(model)
        tr.appendChild(year)
        tr.appendChild(airbags)
        tr.appendChild(color)
        tr.appendChild(photo)
        let tdd = document.createElement("td")
        tdd.appendChild(invoicebt)
        tr.appendChild(tdd)
        if(i.invoice){
            let tddd = document.createElement("td");
            let divv = document.createElement("button");
            divv.classList.add("downloadbtn")
            let a = document.createElement("A")
            a.innerHTML = "Pobierz fakture"
            a.href = "/getinvoice?uuid="+i.uuid
            divv.appendChild(a)
            tddd.appendChild(divv);
            tr.appendChild(tddd)
        }else{
            let tddd = document.createElement("td");
            tr.appendChild(tddd)
        }
        tbody.appendChild(tr)
    })
    let years = await fetchPostAsyncYears()
    console.log(years);
    years.sort()
    let select = document.getElementById("years")
    if(years.length == 0){
        select.style.display = "none";
    }else{
        select.style.display = "block";
        years.map(i=>{
            let opt = document.createElement("option");
            opt.value = i;
            opt.innerText = i;
            select.appendChild(opt)
        })
    }
   
}
fetchPostAsync = async () => {
    const options = {
        method: "GET",
    };

    let response = await fetch("/json", options)

    if (!response.ok)
        return response.status
    else
        return await response.json() // response.json

}
fetchPostAsyncYears = async () => {
    const options = {
        method: "GET",
    };

    let response = await fetch("/getYears", options)

    if (!response.ok)
        return response.status
    else
        return await response.json() // response.json

}
generateInvoiceForAllCars = async() =>{
    const data = JSON.stringify({})
    const options = {
        method: "POST",
        body: data,
    };
    let response = await fetch("/invoiceforallcars", options)
    if (!response.ok)
        return response.status
    else
        getJSONData()
    let body = await response.json()
    console.log(body);
    let div = document.getElementById("allcarinvoices")
    div.innerHTML = "";
    body.map(i=>{
        let str = i.slice(7)
        str = str.slice(0, -4);
        console.log(str);
        let arr = str.split("_")
        console.log(arr);
        let title = `faktura za wszystkie auta -> ${arr[2]}/${arr[1]}/${arr[0]} ${arr[3]}:${arr[4]}:${arr[5]}`;
        let ddiv = document.createElement("div")
        ddiv.classList.add("invoicelink")
        let a = document.createElement("A")
        a.innerHTML = "Faktura"
        a.title = title;
        a.href = "/getAllCarsInvoice?link="+i
        ddiv.appendChild(a)
        div.appendChild(ddiv)
    })
    return body // response.json
}
generateInvoiceForCarsFromYear = async()=>{
    const data = JSON.stringify({
        year: parseInt(document.getElementById("years").value)
    })
    const options = {
        method: "POST",
        body: data,
    };
    let response = await fetch("/invoiceforallcarsyear", options)
    if (!response.ok)
        return response.status
    else
        getJSONData()
    let body = await response.json()
    console.log(body);
    let div = document.getElementById("byyearinvoices")
    div.innerHTML = "";
    body.map(i=>{
        let str = i.slice(7)
        str = str.slice(0, -4);
        let arr = str.split("_")
        let title = `faktura za rocznik: ${document.getElementById("years").value} -> ${arr[2]}/${arr[1]}/${arr[0]} ${arr[3]}:${arr[4]}:${arr[5]}`;
        let ddiv = document.createElement("div")
        ddiv.classList.add("invoicelink")
        let a = document.createElement("A")
        a.innerHTML = "Faktura"
        a.title = title;
        a.href = "/getAllCarsInvoice?link="+i
        ddiv.appendChild(a)
        div.appendChild(ddiv)
    })
    return body // response.json
}

generateInvoicesByPrices = async () =>{
    const data = JSON.stringify({
        from: parseInt(document.getElementById("from").value),
        to: parseInt(document.getElementById("to").value)
    })
    const options = {
        method: "POST",
        body: data,
    };
    let response = await fetch("/invoiceforallcarsprice", options)
    if (!response.ok)
        return response.status
    else
        getJSONData()
    let body = await response.json()
    console.log(body);
    let div = document.getElementById("bypriceinvoices")
    div.innerHTML = "";
    body.map(i=>{
        let str = i.slice(7)
        str = str.slice(0, -4);
        let arr = str.split("_")
        let title = `faktura za rocznik: ${document.getElementById("years").value} -> ${arr[2]}/${arr[1]}/${arr[0]} ${arr[3]}:${arr[4]}:${arr[5]}`;
        let ddiv = document.createElement("div")
        ddiv.classList.add("invoicelink")
        let a = document.createElement("A")
        a.innerHTML = "Faktura"
        a.title = title;
        a.href = "/getAllCarsInvoice?link="+i
        ddiv.appendChild(a)
        div.appendChild(ddiv)
    })
    return body // response.json
}
generateRandomCars = async () =>{
    const options = {
        method: "GET",
    };

    let response = await fetch("/generate", options)

    if (!response.ok)
        return response.status
    else
        getJSONData()
    return await response.json() // response.json
}
getJSONData();