export class AccountRepository {

    getAccountData(){
        return fetch("/account-service/account",{
            method:"GET",
            credentials: 'same-origin'
        }).then(data => data.json());
    }


    save(account){
        return fetch("/account-service/account",{
            method:"PUT",
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(account),
            credentials: 'same-origin'
        }).then(data => data.json());
    }

}