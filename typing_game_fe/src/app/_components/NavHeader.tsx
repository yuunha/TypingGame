
import styled from "styled-components";
import React, { useState, useRef, useEffect } from "react";

const NavHeader: React.FC= () => {

    return(
        <NavContainer>
            <NavBarInner>
                <NavGrid>
                <NavBarLogo style={{ gridArea: "logo" }}> 
                    <a href="/">타.일</a>
                </NavBarLogo>
                <NavBarMenu style={{ gridArea: "menu" }}>
                    <a href="/long">로그인</a>
                    <a href="/game">게임</a>
                </NavBarMenu>
                {/* <div style={{ gridArea: "profile" }}>
                    <img src="/profile.png" alt="Profile" />
                </div> */}
                </NavGrid>
            </NavBarInner>
        </NavContainer>
    );
};


export default NavHeader;

const NavContainer = styled.nav`
    width:100%;
    display: flex;
    align-items:center;
    justify-content: center;
    font-family: NunumHuman, Helvetica, sans-serif;
    font-size : var(--tpg-header-font-size);
    font-weight : bold;
`

const NavBarInner = styled.div`
    width:var(--tpg-basic-width);
    padding: 10px;
    flex-wrap: wrap;
    justify-content: space-between;
    display: flex;
;
`
const NavGrid = styled.div`
  display: grid;
  height: 100%;
  grid-template:
    ". . ." 30px
    "logo menu profile" 46px
    ". . ." / 2fr auto 60px;
  width: 910px;
  margin: auto;
`

const NavBarLogo = styled.div`
;
`
const NavBarMenu = styled.div`
    a{
        padding-left:10px;
    }
`