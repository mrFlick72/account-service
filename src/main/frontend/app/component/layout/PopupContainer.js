import React from "react"
import YesAndNoButtonGroup from "./YesAndNoButtonGroup";

export default ({modal, form, saveFun, noFun}) => {

    return <div className="modal fade" id={modal.id}
                tabIndex="-1" role="dialog"
                aria-labelledby={modal.id + "Title"} aria-hidden="true">
        <div className="modal-dialog modal-dialog-centered" role="document">
            <div className="modal-content">
                <div className="modal-header">
                    <h5 className="modal-title" id={modal.id + "Title"}>{modal.title}</h5>
                    <button type="button" className="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div className="modal-body">
                    {form}
                </div>
                <div className="modal-footer">
                    <YesAndNoButtonGroup
                        buttonMessages={{yesLabel: modal.saveButtonLable, noLabel: modal.closeButtonLable}}
                        yesFun={saveFun} noFun={noFun} yesIcon="fas fa-cart-plus fa-lg"/>
                </div>
            </div>
        </div>
    </div>
}