import React from "react"

export default ({header, title, children}) => {
    return (<div className="card">
        <div className="card-header">
            {header}
        </div>
        <div className="card-body">
            {title && <h5 className="card-title">{title}</h5>}
            <div className="card-text">
                {children}
            </div>
        </div>
    </div>)
}