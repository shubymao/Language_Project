import React from 'react';
import Checkbox from './Checkbox'
function Checklist(){
    return (
        <div>
        
             
        
        <div className="todo-list"> 
        <h2 className="Checklist-title">Daily Checklist</h2>
        <Checkbox item='Bring sun screen'/>
        <Checkbox item='Bring lots of water'/>
        <Checkbox item='Enjoy the Sun'/>
        </div>
        </div>
    );
}

export default Checklist;