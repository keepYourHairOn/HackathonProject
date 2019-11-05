import React, { Component } from "react";

import * as Constants from "../../constants/enums.js";
import '../../styles/GridCell/GridCell.css';

class GridCell extends Component {
    constructor (props) {
        super(props);
        this.state = {
            styles: {
                gridColumn: 'col ' + this.props.column,
                gridRow: 'row ' + this.props.row,
                background: Constants.cellColor[this.props.type],
                gridTemplateRows: '5px',
                gridColumnGap: '0'
            }
        }
    }

    render() {
        return (
            <div className="gridCell" style={this.state.styles}>
            </div>
        );
    }
}

export default GridCell;