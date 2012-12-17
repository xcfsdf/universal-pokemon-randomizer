package dsfunctions;

/*----------------------------------------------------------------------------*/
/*--  TreeNode.java - a linked data structure to help convert PC            --*/
/*--                  filesystems into NDS filesystems.                     --*/
/*--  Code derived from "Nintendo DS rom tool", copyright (C) DevkitPro     --*/
/*--  Original Code by Rafael Vuijk, Dave Murphy, Alexei Karpenko			--*/
/*--																		--*/
/*--  Ported to Java by Dabomstew under the terms of the GPL:				--*/
/*--                                                                        --*/
/*--  This program is free software: you can redistribute it and/or modify  --*/
/*--  it under the terms of the GNU General Public License as published by  --*/
/*--  the Free Software Foundation, either version 3 of the License, or     --*/
/*--  (at your option) any later version.                                   --*/
/*--                                                                        --*/
/*--  This program is distributed in the hope that it will be useful,       --*/
/*--  but WITHOUT ANY WARRANTY; without even the implied warranty of        --*/
/*--  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the          --*/
/*--  GNU General Public License for more details.                          --*/
/*--                                                                        --*/
/*--  You should have received a copy of the GNU General Public License     --*/
/*--  along with this program. If not, see <http://www.gnu.org/licenses/>.  --*/
/*----------------------------------------------------------------------------*/

public class TreeNode {

	int dir_id;
	String name;
	TreeNode directory, prev, next;

	public TreeNode() {
		dir_id = 0;
		name = "";
		directory = prev = next = null;
	}

	public TreeNode New(String name, boolean isdir) {
		TreeNode newNode = new TreeNode();
		newNode.name = name;

		TreeNode node = this;

		if (name.compareTo(node.name) < 0) {
			// prev
			while (name.compareTo(node.name) < 0) {
				if (node.prev != null) {
					node = node.prev;
				} else {
					break; // insert after dummy node
				}
			}
		} else {
			while (node.next != null && (name.compareTo(node.next.name) >= 0)) {
				node = node.next;
			}
		}

		// insert after current node
		newNode.prev = node;
		newNode.next = node.next;
		if (node.next != null) {
			node.next.prev = newNode;
		}
		node.next = newNode;

		return newNode;
	}

}
