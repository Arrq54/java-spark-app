let updateuuid = "";
async function getJSONData() {
    let json = await fetchPostAsync()
    console.log(json)
    let tbody = document.getElementById("tbd")
    tbody.innerHTML = "";

    json.map(i=>{
        let tr = document.createElement("TR");
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
        let colorDiv = document.createElement("div")
        colorDiv.classList.add("color");
        colorDiv.style.backgroundColor = i.color;
        color.appendChild(colorDiv);


        let deleteBt = document.createElement("button")
        deleteBt.innerText = "Usuń"
        deleteBt.classList.add("btn")
        deleteBt.onclick = async () => {
            console.log("AAA")
            let x = await fetchDeleteAsync(i.uuid)
            getJSONData();
        }


        let editBt = document.createElement("button")
        editBt.innerText = "Edytuj"
        editBt.classList.add("btn")
        editBt.onclick = async () => {
            document.getElementById("edtf").style.display = "flex";

            document.getElementById("overlay").style.display = "block";
            updateuuid = i.uuid;


            document.getElementById("updateName").value = i.model;

            let tab = document.getElementsByClassName("opte");
            for(let x=0;x<tab.length;x++){
                console.log(tab[x])
                console.log(i)
                if(i.year == tab[x].value){
                    tab[x].selected = true;
                }
            }


        }

        let forselect  = document.createElement("TD");
        forselect.classList.add("selected")
        tr.appendChild(forselect)


        tr.appendChild(id)
        tr.appendChild(uuid)
        tr.appendChild(model)
        tr.appendChild(year)
        tr.appendChild(airbags)
        tr.appendChild(color)
        let tddd = document.createElement("td")
        tddd.appendChild(deleteBt)
        tr.appendChild(tddd)
        let tdd = document.createElement("td")
        tdd.appendChild(editBt)
        tr.appendChild(tdd)
        tbody.appendChild(tr)

    })

}
hideEdit = () =>{
    document.getElementById("edtf").style.display = "none";
    document.getElementById("overlay").style.display = "none";
}
updateCar = async () =>{
    hideEdit();
    let obj = {
        uuid: updateuuid,
        model: document.getElementById("updateName").value,
        year: document.getElementById("updateYear").value
    }
    const data = JSON.stringify(obj)
    const options = {
        method: "POST",
        body: data,
    };

    let response = await fetch("/update", options)

    if (!response.ok)
        return response.status
    else
        getJSONData()
}
fetchDeleteAsync = async (uuid) => {
    const data = JSON.stringify({
        uuid: uuid

    })
    const options = {
        method: "POST",
        body: data,
    };

    let response = await fetch("/delete", options)

    if (!response.ok)
        return response.status
    else
        return await response.json() // response.json
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
getJSONData();