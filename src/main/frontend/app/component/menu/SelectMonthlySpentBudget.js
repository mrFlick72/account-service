import React from "react";

export default ({monthRegistry, month, year, action}) => (<form className="form-inline my-2 my-lg-0">
    <div className="form-group-lg">
        <select className="form-control mr-sm-2"
                value={month}
                onChange={(event) => {
                    window.location.href = `${action}?choicedMonth=${event.target.value}&year=${year}`;
                }}>
            {monthRegistry.map(month => {return <option key={month.monthValue} value={month.monthValue} label={month.monthLabel}>{month.monthLabel}</option>})}
        </select>
    </div>
</form>);