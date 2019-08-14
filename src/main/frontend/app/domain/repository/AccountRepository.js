export class AccountRepository {

    getAccountData(){
        return fetch("/account/site/user-info",{
            method:"GET",
            credentials: 'same-origin'
        }).then(data => data.json());
    }


    save(account){
        return fetch("/account/site/user-info",{
            method:"PUT",
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(account),
            credentials: 'same-origin'
        }).then(data => data.json());
    }

}