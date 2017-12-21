using System;
using System.IO;

namespace Nikon_Patch {
    public class p {
        static void Main(string[] args) {
            System.IO.Stream fileStream = File.Open(args[0], FileMode.Open, FileAccess.Read, FileShare.None);

            if (fileStream.Length > (48 * 1024 * 1024))
            {
                fileStream.Close();
                return;
            }

            byte[] data = new byte[fileStream.Length];

            if (data == null)
            {
                fileStream.Close();
                return;
            }

            fileStream.Read(data, 0, (int)fileStream.Length);
            fileStream.Close();

            // Test in valid
            Firmware firm = PatchControl.FirmwareMatch(data, PatchLevel.Released);

            if (firm != null)
            {
                string ModelVersion = string.Format("{0}_{1}", firm.Model, firm.Version);
                Console.WriteLine(ModelVersion);

                if (firm.Patches.Count == 0)
                {
                    Console.WriteLine("No Patches presently exist for this 'Model/Firmware Version'");
                }
                else
                {
                    var text = firm.TestPatch();
                    if (text != "") {
                        // hash matched, but patches did not match
                        Console.WriteLine("A sub-patch failed to map to this 'Model/Firmware Version' - Please Report " + text);
                        return;
                    }
                }

                foreach (var p in firm.Patches)
                {
                    Console.WriteLine(p.Name + ": " + p.Description);
                    p.Enabled = true;
                }

                var outfile = File.Open(args[1], FileMode.Create, FileAccess.Write, FileShare.None);
                firm.Patch(outfile);
                outfile.Close();
            }
            else
            {
                Console.WriteLine("No matching 'Model/Firmware Versions' were found");
            }
        }
    }
}
