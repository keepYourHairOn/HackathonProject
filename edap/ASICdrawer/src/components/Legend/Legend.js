import React, { Component } from "react";

import * as Constants from "../../constants/enums.js";
import LegendItem from '../LegendItem/LegendItem.js';
import '../../styles/Legend/Legend.css';

class Legend extends Component {
    constructor(props) {
        super(props);
        this.state = {
            layers: this.props.layers,
            cells: this.drawCellsLegend(),
            wires: this.drawWiresLegend()
        }
    }

    drawCellsLegend() {
        let cells = [];
        let types = Object.keys(Constants.cellTypeCodes);
        for (let i = 2; i < types.length; i++) {
            console.log(types[i]);
            cells.push(<LegendItem type={types[i]} color={Constants.cellColor[Constants.cellTypeCodes[i]]}/>);
        }
        return cells;
    }

    drawWiresLegend() {
        let wires = [];
        let colors = Constants.layerColor;
         for (let i = 0; i < this.props.layers; i++) {
            wires.push(<LegendItem type={1} color={colors[i]} layer={i+1}/>);
         }
         return wires;
    }

    render() {
        console.log(this.state.layers)
        return (
            <div className="legend">
                <div className="title">
                    Legend
                </div>
                {this.state.cells.map(item => item)}
                {this.state.wires.map(item => item)}
            </div>
        );
    }
}

export default Legend;