import React, { Component } from "react";

import * as Constants from "../../constants/enums.js";
import GridCell from '../GridCell/GridCell.js';
import Wire from '../Wire/Wire.js';
import '../../styles/Grid/Grid.css';

class Grid extends Component {
    constructor(props) {
        super(props);
        this.state = {
            gridData: this.props.grid,
            grid: this.drawGrid(),
            styles: {
                width: this.props.grid[0][0].length * 5 + "px"
            }
        }
    }

    drawGrid() {
        let resultingGrid = [];
        let layer;
        let row;
        let cellType;
        for (let i = 0; i < this.props.grid.length; i++) {
            layer = this.props.grid[i];
            for (let j = 0; j < layer.length; j++) {
                row = layer[j];
                for (let k = 0; k < row.length; k++) {
                    cellType = row[k];
                    if (cellType === 1) {
                        resultingGrid.push(<Wire row={j} column={k} layer={i}/>);
                    } else {
                        resultingGrid.push(<GridCell type={Constants.cellTypeCodes[cellType]} row={j} column={k}/>);
                    }
                }
            }
        }
        return resultingGrid;
    }

    render() {
        return (
            <div className="grid" style={this.state.styles}>
                <div className="row">
                    {
                        this.state.grid.map(item => item)
                    }
                </div>
            </div>
        );
    }
}

export default Grid;