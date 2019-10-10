import React from 'react'
import Menu from "./component/menu/Menu";
import {AccountRepository} from "./domain/repository/AccountRepository";
import DatePicker from "react-datetime"
import "react-datetime/css/react-datetime.css"

const links = {
    logOut: "/account/oidc_logout.html",
    home: "/family-budget/index"
};

class AccountDetailsPage extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            messageRegistry: [],
            firstName: "",
            lastName: "",
            phone: "",
            birthDate: "",
            mail: ""
        };

        this.save = this.save.bind(this);
        this.accountRepository = new AccountRepository();

        this.firstNameInputRef = React.createRef();
        this.lastNameInputRef = React.createRef();
        this.phoneInputRef = React.createRef();
        this.birthDateInputRef = React.createRef();
        this.mailInputRef = React.createRef();
    }

    initMessages() {
        this.menuMessages = {
            title: "Only One Portal - Account",
            logOutLabel: "Log Out"
        };
    }

    save() {
        let account = {
            firstName: this.firstNameInputRef.current.value,
            lastName: this.lastNameInputRef.current.value,
            phone: this.phoneInputRef.current.value,
            birthDate: this.birthDateInputRef.current.value
        };

        this.accountRepository.save(account);
    }

    componentDidMount() {
        this.accountRepository.getAccountData()
            .then((data) => {
                this.firstNameInputRef.current.value = data.firstName;
                this.lastNameInputRef.current.value = data.lastName;
                this.phoneInputRef.current.value = data.phone;
                this.birthDateInputRef.current.value = data.birthDate;
                this.mailInputRef.current.value = data.mail;
            })
    }

    render() {
        this.initMessages();

        return (
            <div>
                <Menu messages={this.menuMessages} links={links}></Menu>

                <div className="container">
                    <div className="content">
                        <div className="form-group">
                            <label htmlFor="firstName">First Name:</label>
                            <input type="text" className="form-control" ref={this.firstNameInputRef}
                                   id="firstName" placeholder="First Name"/>
                        </div>
                        <div className="form-group">
                            <label htmlFor="lastName">Last Name:</label>
                            <input type="text" className="form-control" ref={this.lastNameInputRef}
                                   id="lastName" placeholder="First Name"/>
                        </div>

                        <div className="form-group">
                            <label htmlFor="birthDate">Birth Date:</label>
                            <DatePicker inputProps={{id: "birthDate", ref: this.birthDateInputRef}}
                                        input={true}
                                        closeOnSelect={true}
                                        dateFormat="DD/MM/YYYY"
                                        isValidDate={() => true}
                                        timeFormat={false}/>
                        </div>

                        <div className="form-group">
                            <label htmlFor="phone">Phone:</label>
                            <input type="text" className="form-control" ref={this.phoneInputRef}
                                   id="phone" placeholder="Phone"/>
                        </div>

                        <div className="form-group">
                            <label htmlFor="mail">Mail:</label>
                            <input type="text" className="form-control" ref={this.mailInputRef}
                                   id="mail" placeholder="E Mail" readOnly="readonly"/>
                        </div>

                        <div className="form-group">

                            <button type="submit" className="btn btn-success" onClick={this.save}>
                                <i className="fas fa-check fa-lg"></i> Save changes
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}

export default AccountDetailsPage