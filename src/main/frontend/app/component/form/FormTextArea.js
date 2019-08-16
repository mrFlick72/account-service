import React from "react"

export default ({value, onChangeHandler, componentId, componentLabel, componentPlaceholder, row}) => {
    return <div className="form-group">
        <label htmlFor={componentId}>{componentLabel} </label>
        <textarea className="form-control" id={componentId}
                  onChange={onChangeHandler}
                  placeholder={componentPlaceholder} rows={row || 5} value={value}/>
    </div>
}