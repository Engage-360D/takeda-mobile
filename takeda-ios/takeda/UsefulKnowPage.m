//
//  UsefulKnowPage.m
//  takeda
//
//  Created by Alexander Rudenko on 19.03.15.
//  Copyright (c) 2015 organization. All rights reserved.
//

#import "UsefulKnowPage.h"

@interface UsefulKnowPage ()

@end

@implementation UsefulKnowPage
@synthesize infoData;

- (void)viewDidLoad {
    [super viewDidLoad];
    self.view.backgroundColor = RGB(236, 236, 236);
    self.tableView.tableHeaderView = self.tableView.topSepar;
    UIImageView *s = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, self.tableView.width, 0.5)];
    s.backgroundColor = self.tableView.separatorColor;

    [self.danger_text addSubview:s];
    self.tableView.tableFooterView = self.danger_text;
}

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    [self initData];
    
}

-(void)initData{

    NSString *path = [[NSBundle mainBundle] pathForResource:
                      @"UsefulKnowContent" ofType:@"plist"];
    
    infoData = [Global recursiveMutable:[[NSMutableArray alloc] initWithContentsOfFile:path]];
    [self.tableView reloadData];
}


#pragma mark - Table view data source

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return infoData.count;
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    UsefulKnowCell *cell = (UsefulKnowCell*)[self tableView:tableView cellForRowAtIndexPath:indexPath];
    return [cell heightCell:infoData[indexPath.row]];
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *CellIdentifier = @"UsefulKnowCell";
    
    UsefulKnowCell *cell = (UsefulKnowCell *)[self.tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    if(!cell)
    {
        NSArray *topLevelObjects = [[NSBundle mainBundle] loadNibNamed:@"UsefulKnowCell" owner:nil options:nil];
        for(id currentObject in topLevelObjects)
        {
            if([currentObject isKindOfClass:[UsefulKnowCell class]])
            {
                cell = (UsefulKnowCell *)currentObject;
                for (UIButton *btn in cell.shareBtns){
                    [btn addTarget:self action:@selector(shareAction:) forControlEvents:UIControlEventTouchUpInside];
                }
                
                break;
            }
        }
    }
    
    NSMutableDictionary *menu = infoData[indexPath.row];
    [cell setupCell:menu];
    cell.sharePanel.tag = indexPath.row;
    cell.backgroundColor = RGB(236, 236, 236);
    cell.contentView.backgroundColor = RGB(236, 236, 236);

    return cell;
}

#pragma mark - Table view delegate

-(void)shareAction:(UIButton*)sender{
    int index = sender.superview.tag;
    int social = sender.tag;
    
}



@end
