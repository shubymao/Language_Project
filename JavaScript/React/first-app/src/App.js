import React from 'react';
import Content from './components/Content.js';
import Nav from './components/Header.js';
import Footer from './components/Footer.js';
import Clock from './components/Clock'
import Checklist from './components/Checklist'
import Joke from './components/Joke.js';
const input= [
  {question:"What did the Buddhist ask the hot dog vendor?",line:"Make me one with everything"},
  {question: "You know why you never see elephants hiding up in trees?",line:"Because they’re really good at it."},
  {question:"What is red and smells like blue paint?",line:"Red paint."}
];
function App() {
  return (
    <div>
    <Nav />
    <Clock/>
    <Content />
    <Checklist/>
    <Joke props={input[0]}/>
    <Joke props={input[1]}/>
    <Joke props={input[2]}/>
    <Footer />
    </div>
  );
}

export default App;
