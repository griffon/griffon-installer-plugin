%define is_mandrake %(test -e /etc/mandrake-release && echo 1 || echo 0)
%define is_suse %(test -e /etc/SuSE-release && echo 1 || echo 0)
%define is_fedora %(test -e /etc/fedora-release && echo 1 || echo 0)

%define dist redhat
%define disttag rh

%if %is_mandrake
%define dist mandrake
%define disttag mdk
%endif
%if %is_suse
%define dist suse
%define disttag suse
%define kde_path /opt/kde3
%endif
%if %is_fedora
%define dist fedora
%define disttag rhfc
%endif

%define _bindir		%kde_path/bin
%define _datadir	%kde_path/share
%define _iconsdir	%_datadir/icons
%define _docdir		%_datadir/doc
%define _localedir	%_datadir/locale
%define qt_path		/usr/lib/qt3

%define distver %(release="`rpm -q --queryformat='%{VERSION}' %{dist}-release 2> /dev/null | tr . : | sed s/://g`" ; if test $? != 0 ; then release="" ; fi ; echo "$release")
%define distlibsuffix %(%_bindir/kde-config --libsuffix 2>/dev/null)
%define _lib lib%distlibsuffix
%define packer %(finger -lp `echo "$USER"` | head -n 1 | cut -d: -f 3)

Name:           @app.name@
Version:        @app.version@
#Release:        1.%{disttag}%{distver}
Release:        1
License:        @app.license@
Provides:       @app.name@
BuildRoot:      %{_tmppath}/%{name}-%{version}-build
Group:          Applications/@app.name@
Summary:        @app.summary@
Source:         @app.name@-@app.version@-bin.zip
URL:            @app.url@
BuildArch:      noarch
#BuildRequires:  unzip
Packager:       %packer

%description
@app.description@

%prep
%setup -n @app.name@-@app.version@
rm bin/*.bat
rm griffon.icns

%build
echo "nothing to compile"

%install
install -d $RPM_BUILD_ROOT/usr/local/share/@app.name@/
for entry in `ls .`
do
    if (test -d ${entry}) then
        install -d $RPM_BUILD_ROOT/usr/local/share/@app.name@/$entry
        install -p ${entry}/* $RPM_BUILD_ROOT/usr/local/share/@app.name@/$entry
    fi
    if (test -f ${entry}) then
        install -p ${entry} $RPM_BUILD_ROOT/usr/local/share/@app.name@/$entry
    fi
done

%clean
rm -rf "$RPM_BUILD_ROOT"

%post
/sbin/ldconfig

%postun
/sbin/ldconfig

%files
%defattr(-,root,root)
/usr/*

%changelog
