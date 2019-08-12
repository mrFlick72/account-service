import React from "react"

export default ({buttonMessages, yesIcon, yesFun, noFun}) => {
    return <React.Fragment>
        <button type="button" className="btn btn-secondary" data-dismiss="modal" onClick={noFun}>
            <i className="fas fa-times fa-lg"></i> {buttonMessages.noLabel}
        </button>
        <button type="button" className="btn btn-success" onClick={yesFun}>
            <i className={yesIcon}></i> {buttonMessages.yesLabel}
        </button>
    </React.Fragment>
}