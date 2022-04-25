cat prefix-readme.md > ../README.md
cat MainDocumentation.md | sed 's/img src="/img src="documentation\//g' | sed 's/\[\](/\[\](documentation\//g' >> ../README.md 
