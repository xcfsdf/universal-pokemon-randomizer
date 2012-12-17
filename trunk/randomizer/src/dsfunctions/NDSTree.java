package dsfunctions;

/*----------------------------------------------------------------------------*/
/*--  NDSTree.java - contains helper functions to turn a PC filesystem		--*/
/*--  			     into an NDS one.           							--*/
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

import java.io.File;

public class NDSTree {

	public int free_dir_id = 0xF000;
	public int directory_count = 0;
	public int file_count = 0;
	public int total_name_size = 0;

	public int free_file_id = 0;
	public int _entry_start;
	public int file_top;

	public int file_end = 0;

	public TreeNode ReadDirectory(TreeNode node, String path) {
		if (DSFunctions.verboseMode)
			System.out.printf("%s\n", path);

		File dir = new File(path);
		if (!dir.isDirectory() || !dir.canRead()) {
			System.err.printf("Cannot open directory '%s'.\n", path);
			System.exit(1);
			return null;
		}

		for (File de : dir.listFiles()) {
			if (de.getName().charAt(0) == '.') {
				// exclude all directories starting with .
				continue;
			}
			String strbuf = path + "/" + de.getName();

			total_name_size += de.getName().length();

			if (de.isDirectory()) {
				node = node.New(de.getName(), true);
				node.dir_id = free_dir_id++;
				directory_count++;
				node.directory = ReadDirectory(new TreeNode(), strbuf);
			} else if (de.isFile()) {
				node = node.New(de.getName(), false);
				file_count++;
			} else {
				System.err.printf("'%s' is not a file or directory!\n", strbuf);
				System.exit(1);
				return null;
			}
		}

		while (node.prev != null) {
			node = node.prev;
		}

		return node;
	}

}
