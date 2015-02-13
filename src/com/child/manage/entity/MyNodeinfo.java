package com.child.manage.entity;


import szy.utility.NodeInfo;

public class MyNodeinfo extends NodeInfo {
	private static final long serialVersionUID = 1L;
	private int nLevel;// 处于树控件的第几级，最高为0级
	private boolean bExpanded = false;// 节点是否展开

	public MyNodeinfo(NodeInfo nodeInfo) {
		setbAlarmSatas(nodeInfo.isbAlarmSatas());
		setbAlive(nodeInfo.isbAlive());
		setbAtTerm(nodeInfo.isbAtTerm());
		setbCloudDev(nodeInfo.isbCloudDev());
		setbSxtNode(nodeInfo.isbSxtNode());
		setnClaritylist(nodeInfo.getnClaritylist());
		setnJieDianCount(nodeInfo.getnJieDianCount());
		setnSupportclaritylist(nodeInfo.getnSupportclaritylist());
		setnSxtCount(nodeInfo.getnSxtCount());
		setsNodeId(nodeInfo.getsNodeId());
		setsNodeName(nodeInfo.getsNodeName());
		setsParentId(nodeInfo.getsParentId());
	}
	
	public MyNodeinfo(String sNodeId, String sNodeName, boolean bSxtNode,
					  String sParentId, int nLevel, boolean bExpanded, boolean bAlive) {
		setsNodeId(sNodeId);
		setsNodeName(sNodeName);
		setbSxtNode(bSxtNode);
		setsParentId(sParentId);
		setnLevel(nLevel);
		setbExpanded(bExpanded);
		setbAlive(bAlive);
	}

	public int getnLevel() {
		return nLevel;
	}

	public void setnLevel(int nLevel) {
		this.nLevel = nLevel;
	}

	public boolean isbExpanded() {
		return bExpanded;
	}

	public void setbExpanded(boolean bExpanded) {
		this.bExpanded = bExpanded;
	}

}
