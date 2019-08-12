import React from 'react';
import ReactDOM from 'react-dom';
import AccountDetailsPage from "./AccountDetailsPage";

if(document.getElementById('app')){
    ReactDOM.render(<AccountDetailsPage />, document.getElementById('app'));
}