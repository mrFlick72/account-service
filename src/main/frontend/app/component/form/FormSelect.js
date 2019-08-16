import React from "react"
import Select from "react-select";
import 'react-select/dist/react-select.css';

export default ({multi,options, value, onChangeHandler, componentId, componentLabel}) => {
    return <div className="form-group">
        <label htmlFor={componentId}>{componentLabel}</label>
        <Select id={componentId}
                multi={multi}
                value={value}
                onChange={onChangeHandler}
                options={options}/>
    </div>
}