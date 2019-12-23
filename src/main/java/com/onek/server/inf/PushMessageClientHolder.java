// **********************************************************************
//
// Copyright (c) 2003-2016 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************
//
// Ice version 3.6.3
//
// <auto-generated>
//
// Generated from file `iceInterfaces.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package com.onek.server.inf;

public final class PushMessageClientHolder extends Ice.ObjectHolderBase<PushMessageClient>
{
    public
    PushMessageClientHolder()
    {
    }

    public
    PushMessageClientHolder(PushMessageClient value)
    {
        this.value = value;
    }

    public void
    patch(Ice.Object v)
    {
        if(v == null || v instanceof PushMessageClient)
        {
            value = (PushMessageClient)v;
        }
        else
        {
            IceInternal.Ex.throwUOE(type(), v);
        }
    }

    public String
    type()
    {
        return _PushMessageClientDisp.ice_staticId();
    }
}
