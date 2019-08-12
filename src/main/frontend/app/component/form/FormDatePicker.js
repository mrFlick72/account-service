import React from "react"
import DatePicker from "react-datetime"
import "react-datetime/css/react-datetime.css"

export default ({value, onChangeHandler, componentId, componentLabel}) => {
    return (<div className="form-group">
        <label htmlFor={componentId}>{componentLabel}</label>
        <DatePicker inputProps={{id: componentId}}
                    input={true}
                    closeOnSelect={true}
                    value={value}
                    dateFormat="DD/MM/YYYY"
                    isValidDate={() => true}
                    timeFormat={false}
                    onChange={onChangeHandler}/>
    </div>)
}