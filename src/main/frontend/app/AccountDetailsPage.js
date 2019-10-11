import React from 'react'
import Menu from "./component/menu/Menu";
import {AccountRepository} from "./domain/repository/AccountRepository";
import DatePicker from "react-datetime"
import "react-datetime/css/react-datetime.css"
import MessageRepository from "./domain/repository/MessagesRepository";

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
        this.messageRepository = new MessageRepository();

        this.firstNameInputRef = React.createRef();
        this.lastNameInputRef = React.createRef();
        this.phoneInputRef = React.createRef();
        this.birthDateInputRef = React.createRef();
        this.mailInputRef = React.createRef();
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

        this.messageRepository.getMessages()
            .then((data) => {
                this.setState({messageRegistry: data})
            })
    }

    render() {
        return (
            <div>
                <Menu messages={{
                    title: this.messageRepository.getMessagesFor(this.state.messageRegistry, "common.title"),
                    logOutLabel: this.messageRepository.getMessagesFor(this.state.messageRegistry, "logout.label")
                }} links={links}></Menu>

                <div className="container">
                    <div className="content">
                        <div className="form-group">
                            <label
                                htmlFor="firstName">{this.messageRepository.getMessagesFor(this.state.messageRegistry, "form.firstName.label")} </label>
                            <input type="text" className="form-control" ref={this.firstNameInputRef}
                                   id="firstName"
                                   placeholder={this.messageRepository.getMessagesFor(this.state.messageRegistry, "form.firstName.placeholder")}/>
                        </div>
                        <div className="form-group">
                            <label
                                htmlFor="lastName">{this.messageRepository.getMessagesFor(this.state.messageRegistry, "form.lastName.label")}</label>
                            <input type="text" className="form-control" ref={this.lastNameInputRef}
                                   id="lastName"
                                   placeholder={this.messageRepository.getMessagesFor(this.state.messageRegistry, "form.lastName.placeholder")}/>
                        </div>

                        <div className="form-group">
                            <label
                                htmlFor="birthDate">{this.messageRepository.getMessagesFor(this.state.messageRegistry, "form.birthDate.label")}</label>
                            <DatePicker inputProps={{id: "birthDate", ref: this.birthDateInputRef}}
                                        input={true}
                                        closeOnSelect={true}
                                        dateFormat="DD/MM/YYYY"
                                        isValidDate={() => true}
                                        timeFormat={false}/>
                        </div>

                        <div className="form-group">
                            <label
                                htmlFor="phone">{this.messageRepository.getMessagesFor(this.state.messageRegistry, "form.phone.label")}</label>
                            <input type="text" className="form-control" ref={this.phoneInputRef}
                                   id="phone"
                                   placeholder={this.messageRepository.getMessagesFor(this.state.messageRegistry, "form.phone.placeholder")}/>
                        </div>

                        <div className="form-group">
                            <label
                                htmlFor="mail">{this.messageRepository.getMessagesFor(this.state.messageRegistry, "form.mail.label")}</label>
                            <input type="text" className="form-control" ref={this.mailInputRef}
                                   id="mail"
                                   placeholder={this.messageRepository.getMessagesFor(this.state.messageRegistry, "form.mail.placeholder")}
                                   readOnly="readonly"/>
                        </div>

                        <div className="form-group">

                            <button type="submit" className="btn btn-success" onClick={this.save}>
                                <i className="fas fa-check fa-lg"></i> {this.messageRepository.getMessagesFor(this.state.messageRegistry, "form.save.value")}
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        )
    }
}

export default AccountDetailsPage