using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Windows;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Reflection;

namespace Nikon_Patch
{
    public enum PatchLevel
    {
        DevOnly,
        Alpha,
        Beta,
        Released
    }

    public class PatchSet : INotifyPropertyChanged
    {
        public PatchSet(PatchLevel patchLevel, string name, Patch[] _changes, params Patch[][] _incompatible)
        {
            PatchStatus = patchLevel;
            Name = name;
            Enabled = false;
            changes = _changes;
            incompatible = _incompatible;
        }

        public Patch[] changes;
        public Patch[][] incompatible;

        public string Name { get; set; }
        public string Description { get; set; }

        public PatchLevel PatchStatus;
        bool enabled;
        public bool Enabled
        {
            get { return enabled; }
            set
            {
                /*
                if (value == true && incompatible.Length > 0 && patches != null)
                {
                    int i = 0;
                    foreach (var pps in patches.ItemsSource)
                    {
                        foreach (var inc in incompatible)
                        {
                            PatchSet ps = (PatchSet)pps;
                            if (ps.changes == inc && ps.Enabled == true)
                            {
                                ps.Enabled = false;
                            }

                        }
                        i += 1;
                    }

                }
                */
                enabled = value;
                NotifyPropertyChanged("Enabled");
            }
        }

        private void NotifyPropertyChanged(string name)
        {
            if (PropertyChanged != null)
                PropertyChanged(this, new PropertyChangedEventArgs(name));
        }

        public event PropertyChangedEventHandler PropertyChanged;
    }
}
