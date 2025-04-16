import './bootstrap.min.css';
import './App.css';
import React, { useState } from 'react';
import {Market} from './component/Market'
import {Account} from './component/Account'
import {Portfolio} from './component/Portfolio'
import { ToastContainer } from 'react-toastify';

function App() {
  const [activeTab, setActiveTab] = useState('portfolio');

  return (
    <div class="container">
      <Account></Account>
      <div className="tabs">
        <div
          className={`tab ${activeTab === 'portfolio' ? 'active' : ''}`}
          onClick={() => setActiveTab('portfolio')} >
          My Portfolio
        </div>
        <div
          className={`tab ${activeTab === 'market' ? 'active' : ''}`}
          onClick={() => setActiveTab('market')}>
          Market
        </div>
      </div>

      <div className={"tab-content " + (activeTab === 'portfolio' ? "active" : "")} id="portfolio">
        <Portfolio></Portfolio>
      </div>

      <div className={"tab-content " + (activeTab === 'market' ? "active" : "")} id="market">
        <Market></Market>
      </div>
      <ToastContainer />
    </div>
  );
}

export default App;
