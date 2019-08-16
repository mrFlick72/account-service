import React from "react"

export default ({fileRef, componentId, componentLabel}) => {
    return <div className="form-group">
        <label htmlFor={componentId}>{componentLabel} </label>
        <input ref={fileRef} type="file" className="form-control" id={componentId}/>
    </div>
}