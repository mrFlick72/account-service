export default class MessageRepository {

    getMessages() {
        return fetch("/account/messages", {
            method: "GET",
            credentials: 'same-origin'
        }).then(data => data.json());
    }

    getMessagesFor(bundle, label) {
        return bundle ? bundle[label] || "" :  ""
    }
}