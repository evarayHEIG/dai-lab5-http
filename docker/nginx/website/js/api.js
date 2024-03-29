// adapt the url for http or https
const urlApi = window.location.origin + "/api/songs"
let index = 1

function fetchSongs() {
// we fetch backend data
    fetch(urlApi).then(response => {
        // we convert them into json
        response.json().then(data => {
            let song = data[index]
            index = (index + 1) % (Object.values(data).length + 1);
            // create new div
            let div = document.createElement('div')
            // we add data to div
            div.innerHTML = `<p>${song.singer} - ${song.title}</p>`
            // we retrieve the div in which we want to inject the data
            const divToInject = document.getElementsByClassName("api-inject")[0]
            divToInject.innerHTML = ""
            // we add the new div to the dom
            divToInject.appendChild(div)
        })
    })
    .catch(error => console.error('Error fetching data:', error));
}

// Call fetchSongs every 4 seconds
setInterval(fetchSongs, 4000)
