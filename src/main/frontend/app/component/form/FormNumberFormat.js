import React from "react"
import NumberFormat from "react-number-format";

export default ({value, onChangeHandler, componentId, componentLabel}) => {
    return <div className="form-group">
        <label htmlFor={componentId}>{componentLabel}</label>
        <div className="input-group">
            <NumberFormat id={componentId}
                          value={value}
                          className="form-control"
                          decimalScale={2}
                          onChange={onChangeHandler}/>
            <div className="input-group-append">
                                        <span className="input-group-text"
                                              id="insertBudgetModalAmountInputSuffix">&euro;</span>
            </div>
        </div>
    </div>
}