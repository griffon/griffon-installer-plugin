Name:           @app.name@
Version:        @app.version@
Release:        1
License:        !!CHANGE_ME!!
Provides:       @app.name@
BuildRoot:      %{_tmppath}/%{name}-%{version}-build
Group:          Applications/@app.name@
Summary:        !!CHANGE_ME!!
Source:         @app.name@-@app.version@-bin.zip
BuildArch:      noarch
BuildRequires:  unzip
Packager:       Your name <your.account@your.company.com>

%description
!!CHANGE_ME!!

%prep
%setup -n @app.name@-@app.version@
rm bin/*.bat

%build
echo "nothing to compile"

%install
install -d $RPM_BUILD_ROOT/usr/local/share/@app.name@/bin
install -p bin/* $RPM_BUILD_ROOT/usr/local/share/@app.name@/bin
install -d $RPM_BUILD_ROOT/usr/local/share/@app.name@/lib
install -p lib/* $RPM_BUILD_ROOT/usr/local/share/@app.name@/lib

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
