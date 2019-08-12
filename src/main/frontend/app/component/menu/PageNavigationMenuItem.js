import React from "react";
import {Link} from "react-router-dom";

export default ({menuItemLabel, link, menuItemPrefixIcon, menuItemSuffixIcon}) => {
    return  <li className="nav-item active">
        <Link className="nav-link" to={link}>
            <i className={menuItemPrefixIcon}></i> {menuItemLabel} <i className={menuItemSuffixIcon}></i>
        </Link>
    </li>
}