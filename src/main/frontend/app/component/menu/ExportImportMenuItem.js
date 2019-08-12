import React from "react";
import SingleMenuItem from "./SingleMenuItem";

export default ({
                    downloadLink,
                    downloadStentBudgetAsFileUseCaseCsvHandler,
                    downloadStentBudgetAsFileUseCasePdfHandler,
                    downloadStentBudgetAsFileUseCaseXlsxHandler
                }) => {
    return <li className="nav-item dropdown active">
        <a className="nav-link dropdown-toggle" data-toggle="dropdown" href="#"
           role="button" aria-haspopup="true" aria-expanded="false">
            <i className="fas fa-exchange-alt fa-lg"></i>Import/Export
        </a>
        <div className="dropdown-menu">
            <a className="hide" ref={downloadLink} href="#"/>
            <SingleMenuItem key='csv'
                            menuItemLabel='Export To CSV'
                            menuItemPrefixIcon={<i className="fas fa-download fa-lg"></i>}
                            onClickHandler={downloadStentBudgetAsFileUseCaseCsvHandler}/>

            <SingleMenuItem key='pdf'
                            menuItemLabel='Export To Pdf'
                            menuItemPrefixIcon={<i className="fas fa-download fa-lg"></i>}
                            onClickHandler={downloadStentBudgetAsFileUseCasePdfHandler}/>

            <SingleMenuItem key='xlsx'
                            menuItemLabel='Export To Excel'
                            menuItemPrefixIcon={<i className="fas fa-download fa-lg"></i>}
                            onClickHandler={downloadStentBudgetAsFileUseCaseXlsxHandler}/>

            <a href="#" className="dropdown-item" data-toggle="modal" data-target=".loadCsvFile">
                <i className="fas fa-upload fa-lg"></i> Load By CSV
            </a>
        </div>
    </li>
}