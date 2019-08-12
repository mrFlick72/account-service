import React from "react"
import YesAndNoButtonGroup from "./YesAndNoButtonGroup";

export default ({confirmationHandler, modalTitle, modalMessageBody, modalId}) =>
    <div className="modal fade deleteModal" id={modalId} tabIndex="-1" role="dialog"
         aria-labelledby={`${modalId}Title`} aria-hidden="true">
        <div className="modal-dialog modal-dialog-centered" role="document">
            <div className="modal-content">
                <div className="modal-header">
                    <h5 className="modal-title" id={`${modalId}Title`}>{modalTitle}</h5>
                    <button type="button" className="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div className="modal-body">
                    {modalMessageBody}
                </div>
                <div className="modal-footer">
                    <YesAndNoButtonGroup
                        buttonMessages={{yesLabel: "Yes", noLabel: "No"}}
                        yesIcon="fas fa-trash-alt fa-lg" yesFun={confirmationHandler}/>
                </div>
            </div>
        </div>
    </div>