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
             tr.appendChild(forselect)
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
            tr.appendChild(id)
            tr.appendChild(uuid)
            tr.appendChild(model)
            tr.appendChild(year)
            tr.appendChild(airbags)
            tr.appendChild(color)
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


            let uploader = document.createElement("td");
            let uploaderbutton = document.createElement("button");
            uploaderbutton.classList.add("uploadbtn")
            let auploader = document.createElement("a");
            auploader.innerHTML = "Uploader"
            auploader.href = "/upload?uuid="+i.uuid;
            uploaderbutton.appendChild(auploader)
            uploader.appendChild(uploaderbutton)
            tr.appendChild(uploader)


            let gallery = document.createElement("td");
            let gallerybutton = document.createElement("button");
            gallerybutton.classList.add("gallerybtn")
            let agallery = document.createElement("a");
            agallery.innerHTML = "Gallery"
            agallery.href = "/gallery?uuid="+i.uuid;
            gallerybutton.appendChild(agallery)
            gallery.appendChild(gallerybutton)
            tr.appendChild(gallery)

            tbody.appendChild(tr)
        })
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