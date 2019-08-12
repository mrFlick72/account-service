import React from "react";

export default ({onClickHandler, menuItemLabel, menuItemPrefixIcon, menuItemSuffixIcon}) => {
    return <a className="dropdown-item" onClick={onClickHandler} href="#">
        {menuItemPrefixIcon} {menuItemLabel} {menuItemSuffixIcon}
    </a>
}