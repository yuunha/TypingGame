'use client';

import styled from "styled-components";
import React, { useState, useRef, useEffect } from "react";

const NavHeader: React.FC= () => {

    return(
        <NavContainer>
            <NavBarInner>
                <NavBarLogo> 
                    <a href="/">TYLE</a>
                </NavBarLogo>
                <NavBarMenu>
                    <a href="/login">Account</a>
                    <a href="/rank">Rank</a>
                    <a href="/profile">Profile</a>
                </NavBarMenu>
            </NavBarInner>
        </NavContainer>
    );
};


export default NavHeader;

const NavContainer = styled.nav`
    width:100%;
    margin-bottom : 50px;
    display: flex;
    align-items:center;
    justify-content: center;
    font-size : var(--tpg-header-font-size);
`

const NavBarInner = styled.div`
  width: var(--tpg-basic-width);
  padding: 10px;
  display: flex;
  justify-content: space-between;
  align-items: center;
;
`
const NavBarLogo = styled.div`
    font-family: Libertinus, Helvetica, sans-serif;
    font-weight : bold;
;
`
const NavBarMenu = styled.div`
    font-family: Montserrat, Helvetica, sans-serif;
    a{
    margin-left: 12px;
    }
`