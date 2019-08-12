export class AccountRepository {

    getAccountData(){
        return fetch("/site/account-service/account",{
            method:"GET",
            credentials: 'same-origin'
        }).then(data => data.json());
    }


    save(account){
        return fetch("/site/account-service/account",{
            method:"PUT",
            body: JSON.stringify(account),
            credentials: 'same-origin'
        }).then(data => data.json());
    }

}