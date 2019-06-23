import React from 'react';
function Checkbox(props){
    return (
        <div className="todo-item">
        <input type = "checkbox" />
        <p>{props.item}</p>
        </div>
    );
}

export default Checkbox;